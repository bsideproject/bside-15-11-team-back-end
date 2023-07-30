package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.relationship.service.RelationshipService;
import com.beside.startrail.relationship.validator.RelationshipRequestValidator;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.relationship.RelationshipPostRequestProto;
import reactor.core.publisher.Mono;

@Component
public class RelationshipPostHandler extends AbstractSignedTransactionalHandler {
  private final RelationshipService relationshipService;
  private final RelationshipRequestValidator relationshipRequestValidator;

  public RelationshipPostHandler(
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
    return serverRequest
        .bodyToMono(String.class)
        .flatMap(body -> ProtocolBufferUtil.<RelationshipPostRequestProto>parse(
                body, RelationshipPostRequestProto.newBuilder()
            )
        )
        .doOnNext(relationshipRequestValidator::createValidate)
        .flatMap(friendDto -> relationshipService.create(
                super.jwtPayloadProto.getSequence(),
                friendDto
            )
        )
        .map(relationshipResponseProtos ->
            relationshipResponseProtos.stream()
                .map(ProtocolBufferUtil::print)
                .collect(Collectors.toList())
        )
        .flatMap(body ->
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
        );
  }
}
