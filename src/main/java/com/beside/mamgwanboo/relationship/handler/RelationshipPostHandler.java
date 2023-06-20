package com.beside.mamgwanboo.relationship.handler;

import com.beside.mamgwanboo.common.handler.AbstractSignedHandler;
import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.common.util.ProtocolBufferUtil;
import com.beside.mamgwanboo.relationship.repository.RelationshipRepository;
import com.beside.mamgwanboo.relationship.service.RelationshipService;
import com.beside.mamgwanboo.relationship.util.RelationshipDtoUtil;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.relationship.RelationshipPostRequest;
import reactor.core.publisher.Mono;

@Component
public class RelationshipPostHandler extends AbstractSignedHandler {
  private final RelationshipRepository relationshipRepository;

  public RelationshipPostHandler(
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
            ProtocolBufferUtil.<RelationshipPostRequest>parse(
                body,
                RelationshipPostRequest.newBuilder()
            )
        )
        .map(RelationshipPostRequest::getRelationshipsList)
        .map(relationshipDtos ->
            relationshipDtos.stream()
                .map(relationshipDto ->
                    RelationshipDtoUtil.toRelationship(
                        relationshipDto,
                        super.mamgwanbooJwtPayload.getSequence(),
                        YnType.Y
                    )
                )
                .collect(Collectors.toList())
        )
        .flatMap(relationships -> RelationshipService.save(relationships)
            .execute(relationshipRepository)
            .map(RelationshipDtoUtil::toRelationshipResponseDto)
            .map(ProtocolBufferUtil::print)
            .collect(Collectors.joining())
        )
        .flatMap(body ->
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
        );
  }
}
