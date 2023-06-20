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
import reactor.core.publisher.Mono;

@Component
public class RelationshipDeleteHandler extends AbstractSignedHandler {
  private final RelationshipRepository relationshipRepository;

  public RelationshipDeleteHandler(
      @Value("${sign.cookieName}") String cookieName,
      RelationshipRepository relationshipRepository
  ) {
    super(cookieName);
    this.relationshipRepository = relationshipRepository;
  }

  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    String sequence = serverRequest.pathVariable("sequence");

    return RelationshipService
        .remove(sequence)
        .execute(relationshipRepository)
        .map(RelationshipDtoUtil::toRelationshipResponseDto)
        .map(ProtocolBufferUtil::print)
        .flatMap(body ->
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body));
  }
}
