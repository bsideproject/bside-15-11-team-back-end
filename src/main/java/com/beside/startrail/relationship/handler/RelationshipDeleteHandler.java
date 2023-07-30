package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.relationship.service.RelationshipService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RelationshipDeleteHandler extends AbstractSignedTransactionalHandler {
  private final RelationshipService relationshipService;

  public RelationshipDeleteHandler(
      @Value("${sign.attributeName}") String attributeName,
      RelationshipService relationshipService
  ) {
    super(attributeName);
    this.relationshipService = relationshipService;
  }


  @Override
  protected Mono<ServerResponse> signedTransactionalHandle(ServerRequest serverRequest) {
    String sequence = serverRequest.pathVariable("sequence");

    return relationshipService
        .remove(super.jwtPayloadProto.getSequence(), sequence)
        .then(Mono.defer(
                () -> ServerResponse
                    .ok()
                    .build()
            )
        );
  }
}
