package com.beside.mamgwanboo.common.handler;

import com.mongodb.lang.NonNull;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.sign.MamgwanbooJwtPayload;
import reactor.core.publisher.Mono;

public abstract class AbstractSignedHandler implements HandlerFunction<ServerResponse> {
  private final String cookieName;
  protected MamgwanbooJwtPayload mamgwanbooJwtPayload;

  public AbstractSignedHandler(String cookieName) {
    this.cookieName = cookieName;
  }

  @NonNull
  @Override
  public Mono<ServerResponse> handle(ServerRequest request) {
    Optional<Object> optionalMamgwanbooJwtPayload = request.attribute(cookieName);
    if (optionalMamgwanbooJwtPayload.isEmpty()) {
      return ServerResponse
          .status(HttpStatus.UNAUTHORIZED)
          .build();
    }

    mamgwanbooJwtPayload = (MamgwanbooJwtPayload) optionalMamgwanbooJwtPayload.get();

    return signedHandle(request);
  }

  protected abstract Mono<ServerResponse> signedHandle(ServerRequest serverRequest);
}
