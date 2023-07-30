package com.beside.startrail.relationship.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.relationship.service.RelationshipService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.relationship.RelationshipGetRequestProto;
import reactor.core.publisher.Mono;

@Component
public class RelationshipGetBySequenceAndKeywordAndSortHandler extends AbstractSignedHandler {
  private final RelationshipService relationshipService;

  public RelationshipGetBySequenceAndKeywordAndSortHandler(
      @Value("${sign.attributeName}") String attributeName,
      RelationshipService relationshipService
  ) {
    super(attributeName);
    this.relationshipService = relationshipService;
  }


  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    return makeCriteriaParams(serverRequest)
        .flatMapMany(relationshipGetRequestProto ->
            relationshipService.get(
                super.jwtPayloadProto.getSequence(),
                relationshipGetRequestProto.getKeyword(),
                relationshipGetRequestProto.getSort()
            )
        )
        .map(ProtocolBufferUtil::print)
        .collectList()
        .flatMap(body ->
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
        );
  }

  private Mono<RelationshipGetRequestProto> makeCriteriaParams(ServerRequest serverRequest) {
    RelationshipGetRequestProto.Builder builder = RelationshipGetRequestProto
        .newBuilder()
        .clear();

    serverRequest.queryParams()
        .forEach((key, value) -> {
          if (Objects.nonNull(RelationshipGetRequestProto.getDescriptor().findFieldByName(key))) {
            builder.setField(
                RelationshipGetRequestProto.getDescriptor().findFieldByName(key),
                value.stream().findFirst().orElse("")
            );
          }
        });

    return Mono.just(builder.build());
  }
}
