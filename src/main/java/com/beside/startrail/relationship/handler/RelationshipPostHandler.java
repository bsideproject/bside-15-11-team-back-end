package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.relationship.RelationshipProtoUtil;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import com.beside.startrail.relationship.service.RelationshipService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.relationship.RelationshipPostRequestProto;
import reactor.core.publisher.Mono;

@Component
public class RelationshipPostHandler extends AbstractSignedTransactionalHandler {
  private final RelationshipRepository relationshipRepository;

  public RelationshipPostHandler(
      @Value("${sign.attributeName}") String attributeName,
      RelationshipRepository relationshipRepository
  ) {
    super(attributeName);
    this.relationshipRepository = relationshipRepository;
  }

  @Override
  protected Mono<ServerResponse> signedTransactionalHandle(ServerRequest serverRequest) {
    return serverRequest
        .bodyToMono(String.class)
        .flatMap(body -> ProtocolBufferUtil.<RelationshipPostRequestProto>parse(
                body, RelationshipPostRequestProto.newBuilder()
            )
        )
        .flatMapMany(relationshipPostRequestProto ->
            RelationshipProtoUtil.toRelationships(
                super.jwtPayloadProto.getSequence(),
                relationshipPostRequestProto
            )
        )
        .map(RelationshipService::create)
        .flatMap(relationshipSaveOneCommand ->
            relationshipSaveOneCommand.execute(relationshipRepository)
        )
        .map(RelationshipProtoUtil::toRelationshipResponseProto)
        .collectList()
        .flatMap(body ->
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
        );
  }
}
