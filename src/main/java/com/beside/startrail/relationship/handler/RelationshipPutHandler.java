package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.relationship.service.RelationshipService;
import com.beside.startrail.relationship.validator.RelationshipRequestValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.relationship.RelationshipPutRequestProto;
import reactor.core.publisher.Mono;

@Component
public class RelationshipPutHandler extends AbstractSignedTransactionalHandler {
  private final RelationshipService relationshipService;
  private final RelationshipRequestValidator relationshipRequestValidator;

  public RelationshipPutHandler(
      @Value("${sign.attributeName}") String attributeName,
      RelationshipService relationshipService,
      RelationshipRequestValidator relationshipRequestValidator
  ) {
    super(attributeName);
    this.relationshipService = relationshipService;
    this.relationshipRequestValidator = relationshipRequestValidator;
  }

  @Override
  protected Mono<ServerResponse> signedTransactionalHandle(ServerRequest serverRequest) {
    String sequence = serverRequest.pathVariable("sequence");

    return serverRequest
        .bodyToMono(String.class)
        .flatMap(
            body -> ProtocolBufferUtil.<RelationshipPutRequestProto>parse(
                body,
                RelationshipPutRequestProto.newBuilder()
            )
        )
        .doOnNext(relationshipRequestValidator::updateValidate)
        .flatMap(body ->
            relationshipService.update(super.jwtPayloadProto.getSequence(), sequence, body)
        )
        .map(ProtocolBufferUtil::print)
        .flatMap(body ->
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
        );
  }
}
