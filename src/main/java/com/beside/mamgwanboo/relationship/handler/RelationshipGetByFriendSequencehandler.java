package com.beside.mamgwanboo.relationship.handler;

import com.beside.mamgwanboo.common.handler.AbstractSignedHandler;
import com.beside.mamgwanboo.common.util.ProtocolBufferUtil;
import com.beside.mamgwanboo.relationship.repository.RelationshipRepository;
import com.beside.mamgwanboo.relationship.service.RelationshipService;
import com.beside.mamgwanboo.relationship.util.RelationshipDtoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.relationship.RelationshipGetRequest;
import protobuf.relationship.RelationshipGetResponse;
import reactor.core.publisher.Mono;

@Component
public class RelationshipGetByFriendSequencehandler extends AbstractSignedHandler {
  private final RelationshipRepository relationshipRepository;

  public RelationshipGetByFriendSequencehandler(
      @Value("${sign.cookieName}") String cookieName,
      RelationshipRepository relationshipRepository
  ) {
    super(cookieName);
    this.relationshipRepository = relationshipRepository;
  }

  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    return ProtocolBufferUtil
        .<RelationshipGetRequest>from(serverRequest.queryParams(),
            RelationshipGetRequest.newBuilder())
        .map(relationshipGetRequest ->
            RelationshipService.getByFriendSequence(
                relationshipGetRequest.getFriendSequence(),
                relationshipGetRequest.getSort()
            )
        )
        .flatMap(relationshipFindAllByFriendSequenceCommand ->
            relationshipFindAllByFriendSequenceCommand.execute(relationshipRepository)
                .map(RelationshipDtoUtil::toRelationshipResponseDto)
                .collectList()
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
        );
  }
}
