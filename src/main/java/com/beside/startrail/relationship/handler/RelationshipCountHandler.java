package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.relationship.RelationshipProtoUtil;
import com.beside.startrail.relationship.repository.CustomRelationshipRepository;
import com.beside.startrail.relationship.service.RelationshipService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.relationship.RelationshipCountResponseProto;
import reactor.core.publisher.Mono;

@Component
public class RelationshipCountHandler extends AbstractSignedHandler {
  private final CustomRelationshipRepository customRelationshipRepository;

  public RelationshipCountHandler(
      @Value("${sign.attributeName}") String attributeName,
      CustomRelationshipRepository customRelationshipRepository
  ) {
    super(attributeName);
    this.customRelationshipRepository = customRelationshipRepository;
  }

  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    return RelationshipService
        .countByUserSequenceAndRelationshipType(super.jwtPayloadProto.getSequence())
        .execute(customRelationshipRepository)
        .map(RelationshipProtoUtil::toRelationshipCountResponseProto)
        .flatMap(relationshipCountResponseProto ->
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ProtocolBufferUtil.print(relationshipCountResponseProto))
        )
        .switchIfEmpty(
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ProtocolBufferUtil.print(
                    RelationshipCountResponseProto.newBuilder()
                        .setTaken(0)
                        .setGiven(0)
                        .setTaken(0)
                        .build()
                ))
        );
  }
}
