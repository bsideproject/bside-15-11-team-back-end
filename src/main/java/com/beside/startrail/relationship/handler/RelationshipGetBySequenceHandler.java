package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.relationship.service.RelationshipService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RelationshipGetBySequenceHandler extends AbstractSignedHandler {
  private final RelationshipService relationshipService;

  public RelationshipGetBySequenceHandler(
      @Value("${sign.attributeName}") String attributeName,
      RelationshipService relationshipService
  ) {
    super(attributeName);
    this.relationshipService = relationshipService;
  }

  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    String sequence = serverRequest.pathVariable("sequence");

    return relationshipService
        .getBySequence(super.jwtPayloadProto.getSequence(), sequence)
        .flatMap(relationshipResponseProto ->
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ProtocolBufferUtil.print(relationshipResponseProto))
        );
  }
}
