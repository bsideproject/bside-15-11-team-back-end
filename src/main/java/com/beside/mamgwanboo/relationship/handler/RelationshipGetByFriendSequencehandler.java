package com.beside.mamgwanboo.relationship.handler;

import com.beside.mamgwanboo.common.handler.AbstractSignedHandler;
import com.beside.mamgwanboo.common.util.ProtocolBufferUtil;
import com.beside.mamgwanboo.relationship.repository.CustomRelationshipRepository;
import com.beside.mamgwanboo.relationship.service.RelationshipService;
import com.beside.mamgwanboo.relationship.util.RelationshipDtoUtil;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.relationship.RelationshipGetRequest;
import protobuf.relationship.RelationshipGetResponse;
import reactor.core.publisher.Mono;

@Component
public class RelationshipGetByFriendSequencehandler extends AbstractSignedHandler {
  private final CustomRelationshipRepository customRelationshipRepository;

  public RelationshipGetByFriendSequencehandler(
      @Value("${sign.cookieName}") String cookieName,
      CustomRelationshipRepository customRelationshipRepository
  ) {
    super(cookieName);
    this.customRelationshipRepository = customRelationshipRepository;
  }

  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    return ProtocolBufferUtil
        .<RelationshipGetRequest>from(serverRequest.queryParams(),
            RelationshipGetRequest.newBuilder())
        .map(relationshipGetRequest ->
            RelationshipService.getByFriendSequence(
                UUID.fromString(relationshipGetRequest.getFriendSequence()),
                relationshipGetRequest.getSort()
            )
        )
        .flatMap(relationshipFindAllByFriendSequenceCommand ->
            relationshipFindAllByFriendSequenceCommand.execute(customRelationshipRepository)
                .map(RelationshipDtoUtil::toRelationshipDto)
                .collectList()
        )
        .map(relationshipDtos ->
            RelationshipGetResponse.newBuilder()
                .addAllRelationships(relationshipDtos)
                .build()
        )
        .flatMap(relationshipGetResponse ->
            ServerResponse
                .ok()
                .bodyValue(relationshipGetResponse)
        );
  }
}
