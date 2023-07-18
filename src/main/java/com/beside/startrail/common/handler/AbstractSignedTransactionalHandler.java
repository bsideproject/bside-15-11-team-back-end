package com.beside.startrail.common.handler;

import com.mongodb.lang.NonNull;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.sign.JwtPayloadProto;
import reactor.core.publisher.Mono;

public abstract class AbstractSignedTransactionalHandler
    implements HandlerFunction<ServerResponse> {
  private final String attributeName;
  protected JwtPayloadProto jwtPayloadProto;

  public AbstractSignedTransactionalHandler(String attributeName) {
    this.attributeName = attributeName;
  }

  @Transactional
  @NonNull
  @Override
  public Mono<ServerResponse> handle(ServerRequest request) {
    Optional<Object> optionalJwtPayload = request.attribute(attributeName);
    if (optionalJwtPayload.isEmpty()) {
      return ServerResponse
          .status(HttpStatus.UNAUTHORIZED)
          .build();
    }

    jwtPayloadProto = (JwtPayloadProto) optionalJwtPayload.get();

    return signedTransactionalHandle(request);
  }

  protected abstract Mono<ServerResponse> signedTransactionalHandle(ServerRequest serverRequest);
}
