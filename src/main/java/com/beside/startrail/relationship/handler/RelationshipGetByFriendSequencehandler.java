package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.util.ProtocolBufferUtil;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import com.beside.startrail.relationship.service.RelationshipService;
import com.beside.startrail.relationship.util.RelationshipDtoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.relationship.RelationshipGetRequest;
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
        .<RelationshipGetRequest>from(serverRequest.queryParams(),
            RelationshipGetRequest.newBuilder())
        .map(relationshipGetRequest ->
            RelationshipService.getByFriendSequence(
                super.jwtPayload.getSequence(),
                relationshipGetRequest.getFriendSequence(),
                relationshipGetRequest.getSort()
            )
        )
        .flatMap(relationshipFindAllByFriendSequenceCommand ->
            relationshipFindAllByFriendSequenceCommand.execute(relationshipRepository)
                .map(RelationshipDtoUtil::toRelationshipResponseDto)
                .collectList()
        )
        .filter(relationshipResponseDtos ->
            !CollectionUtils.isEmpty(relationshipResponseDtos)
        )
        .map(relationshipResponseDtos ->
            RelationshipGetResponse.newBuilder()
                .addAllRelationships(relationshipResponseDtos)
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
