package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.relationship.RelationshipProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import com.beside.startrail.relationship.service.RelationshipService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.relationship.RelationshipPutRequestProto;
import reactor.core.publisher.Mono;

@Component
public class RelationshipPutHandler extends AbstractSignedTransactionalHandler {
  private final RelationshipRepository relationshipRepository;

  public RelationshipPutHandler(
      @Value("${sign.attributeName}") String attributeName,
      RelationshipRepository relationshipRepository
  ) {
    super(attributeName);
    this.relationshipRepository = relationshipRepository;
  }

  @Override
  protected Mono<ServerResponse> signedTransactionalHandle(ServerRequest serverRequest) {
    String sequence = serverRequest.pathVariable("sequence");

    return serverRequest
        .bodyToMono(String.class)
        .flatMap(
            body -> ProtocolBufferUtil.<RelationshipPutRequestProto>parse(
                body,
                RelationshipPutRequestProto.newBuilder()
            )
        )
        .flatMap(relationshipPutRequestProto ->
            RelationshipService.getByUserSequenceAndSequenceAndUseYn(
                    super.jwtPayloadProto.getSequence(),
                    sequence,
                    YnType.Y
                )
                .execute(relationshipRepository)
                .mapNotNull(relationship ->
                    RelationshipProtoUtil.toRelationship(
                        relationship,
                        relationshipPutRequestProto
                    )
                )
        )
        .flatMap(relationship ->
            RelationshipService
                .create(relationship)
                .execute(relationshipRepository)
        )
        .map(RelationshipProtoUtil::toRelationshipResponseProto)
        .map(ProtocolBufferUtil::print)
        .flatMap(body ->
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
        )
        .switchIfEmpty(
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .build()
        );
  }
}
