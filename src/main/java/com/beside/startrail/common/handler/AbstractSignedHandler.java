package com.beside.startrail.common.handler;

import com.mongodb.lang.NonNull;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.sign.JwtPayload;
import reactor.core.publisher.Mono;

public abstract class AbstractSignedHandler implements HandlerFunction<ServerResponse> {
  protected JwtPayload jwtPayload;

  private final String attributeName;

  public AbstractSignedHandler(
      String attributeName) {
    this.attributeName = attributeName;
  }

  @NonNull
  @Override
  public Mono<ServerResponse> handle(ServerRequest request) {
    Optional<Object> optionalJwtPayload = request.attribute(attributeName);
    if (optionalJwtPayload.isEmpty()) {
      return ServerResponse
          .status(HttpStatus.UNAUTHORIZED)
          .build();
    }

    jwtPayload = (JwtPayload) optionalJwtPayload.get();

    return signedHandle(request);
  }

  protected abstract Mono<ServerResponse> signedHandle(ServerRequest serverRequest);
}
