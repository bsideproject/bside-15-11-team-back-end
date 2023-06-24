package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.common.DateProtoUtil;
import com.beside.startrail.common.protocolbuffer.common.ItemProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import com.beside.startrail.relationship.service.RelationshipService;
import com.beside.startrail.relationship.type.RelationshipType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.common.type.RelationshipTypeProto;
import protobuf.relationship.RelationshipPutRequestProto;
import protobuf.relationship.RelationshipPutResponseProto;
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
    return serverRequest
        .bodyToMono(String.class)
        .flatMap(body ->
            ProtocolBufferUtil.<RelationshipPutRequestProto>parse(
                body,
                RelationshipPutRequestProto.newBuilder()
            )
        )
        .map(this::toRelationship)
        .map(RelationshipService::update)
        .flatMap(relationshipSaveCommand ->
            relationshipSaveCommand.execute(relationshipRepository)
        )
        .map(this::toRelationshipPutResponseProto)
        .map(ProtocolBufferUtil::print)
        .flatMap(body ->
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
        )
        .switchIfEmpty(
            ServerResponse
                .noContent()
                .build()
        );
  }

  private Relationship toRelationship(RelationshipPutRequestProto relationshipPutRequestProto) {
    return Relationship.builder()
        .userSequence(super.jwtPayloadProto.getSequence())
        .sequence(relationshipPutRequestProto.getSequence())
        .type(
            RelationshipType.valueOf(relationshipPutRequestProto.getType().name())
        )
        .event(relationshipPutRequestProto.getEvent())
        .date(DateProtoUtil.toLocalDateTime(relationshipPutRequestProto.getDate()))
        .item(ItemProtoUtil.toItem(relationshipPutRequestProto.getItem()))
        .memo(relationshipPutRequestProto.getMemo())
        .useYn(YnType.Y)
        .build();
  }

  private RelationshipPutResponseProto toRelationshipPutResponseProto(Relationship relationship) {
    return RelationshipPutResponseProto.newBuilder()
        .setSequence(relationship.getSequence())
        .setType(
            RelationshipTypeProto.valueOf(relationship.getType().name())
        )
        .setEvent(relationship.getEvent())
        .setDate(DateProtoUtil.toDate(relationship.getDate()))
        .setItem(ItemProtoUtil.toItemProto(relationship.getItem()))
        .setMemo(relationship.getMemo())
        .build();
  }
}
