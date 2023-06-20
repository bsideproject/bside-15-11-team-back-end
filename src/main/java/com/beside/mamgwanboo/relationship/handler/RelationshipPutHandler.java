package com.beside.mamgwanboo.relationship.handler;

import com.beside.mamgwanboo.common.handler.AbstractSignedHandler;
import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.common.util.DateUtil;
import com.beside.mamgwanboo.common.util.ProtocolBufferUtil;
import com.beside.mamgwanboo.relationship.document.Relationship;
import com.beside.mamgwanboo.relationship.repository.RelationshipRepository;
import com.beside.mamgwanboo.relationship.service.RelationshipService;
import com.beside.mamgwanboo.relationship.util.ItemUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.relationship.RelationshipPutRequest;
import protobuf.relationship.RelationshipPutResponse;
import reactor.core.publisher.Mono;

@Component
public class RelationshipPutHandler extends AbstractSignedHandler {
  private final RelationshipRepository relationshipRepository;

  public RelationshipPutHandler(
      @Value("${sign.cookieName}") String cookieName,
      RelationshipRepository relationshipRepository
  ) {
    super(cookieName);
    this.relationshipRepository = relationshipRepository;
  }

  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    return serverRequest
        .bodyToMono(String.class)
        .flatMap(body ->
            ProtocolBufferUtil.<RelationshipPutRequest>parse(
                body,
                RelationshipPutRequest.newBuilder()
            )
        )
        .map(this::toRelationship)
        .map(RelationshipService::update)
        .flatMap(relationshipSaveCommand ->
            relationshipSaveCommand.execute(relationshipRepository)
        )
        .map(this::toRelationshipPutResponse)
        .map(ProtocolBufferUtil::print)
        .flatMap(body ->
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
        );
  }

  private Relationship toRelationship(RelationshipPutRequest relationshipPutRequest) {
    return Relationship.builder()
        .userSequence(super.mamgwanbooJwtPayload.getSequence())
        .sequence(relationshipPutRequest.getSequence())
        .type(relationshipPutRequest.getType())
        .event(relationshipPutRequest.getEvent())
        .date(DateUtil.toLocalDateTime(relationshipPutRequest.getDate()))
        .item(ItemUtil.toItem(relationshipPutRequest.getItem()))
        .memo(relationshipPutRequest.getMemo())
        .useYn(YnType.Y)
        .build();
  }

  private RelationshipPutResponse toRelationshipPutResponse(Relationship relationship) {
    return RelationshipPutResponse.newBuilder()
        .setSequence(relationship.getSequence())
        .setType(relationship.getType())
        .setEvent(relationship.getEvent())
        .setDate(DateUtil.toDate(relationship.getDate()))
        .setItem(ItemUtil.toItemDto(relationship.getItem()))
        .setMemo(relationship.getMemo())
        .build();
  }
}
