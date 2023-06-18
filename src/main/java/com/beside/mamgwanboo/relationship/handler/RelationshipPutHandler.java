package com.beside.mamgwanboo.relationship.handler;

import com.beside.mamgwanboo.common.handler.AbstractSignedHandler;
import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.common.util.ProtocolBufferUtil;
import com.beside.mamgwanboo.relationship.repository.RelationshipRepository;
import com.beside.mamgwanboo.relationship.service.RelationshipService;
import com.beside.mamgwanboo.relationship.util.RelationshipDtoUtil;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.relationship.RelationshipPutRequest;
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
        .map(RelationshipPutRequest::getRelationshipsList)
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
            .collectList()
            .map(successRelationships ->
                RelationshipService.makeRelationshipPutResponse(
                    relationships,
                    successRelationships
                )
            )
            .flatMap(relationshipPutResponse ->
                ServerResponse
                    .ok()
                    .bodyValue(relationshipPutResponse)
            )
        );
  }
}
