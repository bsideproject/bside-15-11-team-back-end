package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.relationship.RelationshipProtoUtil;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import com.beside.startrail.relationship.service.RelationshipService;
import com.beside.startrail.relationship.type.SortOrderType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.relationship.RelationshipGetRequestProto;
import protobuf.relationship.RelationshipGetResponse;
import reactor.core.publisher.Mono;

@Component
public class RelationshipGetByFriendSequencehandler extends AbstractSignedHandler {
  private final RelationshipRepository relationshipRepository;

  public RelationshipGetByFriendSequencehandler(
      @Value("${sign.attributeName}") String attributeName,
      RelationshipRepository relationshipRepository
  ) {
    super(attributeName);
    this.relationshipRepository = relationshipRepository;
  }

  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    return ProtocolBufferUtil
        .<RelationshipGetRequestProto>from(serverRequest.queryParams(),
            RelationshipGetRequestProto.newBuilder())
        .map(relationshipGetRequestProto ->
            RelationshipService.getByFriendSequence(
                super.jwtPayloadProto.getSequence(),
                relationshipGetRequestProto.getFriendSequence(),
                SortOrderType.valueOf(relationshipGetRequestProto.getSort().name())
            )
        )
        .flatMap(relationshipFindAllByFriendSequenceCommand ->
            relationshipFindAllByFriendSequenceCommand.execute(relationshipRepository)
                .map(RelationshipProtoUtil::toRelationshipResponseProto)
                .collectList()
        )
        .filter(relationshipResponseProtos ->
            !CollectionUtils.isEmpty(relationshipResponseProtos)
        )
        .map(relationshipResrelationshipResponseProtosonseDtos ->
            RelationshipGetResponse.newBuilder()
                .addAllRelationships(relationshipResrelationshipResponseProtosonseDtos)
                .build()
        )
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
}
