package com.beside.mamgwanboo.sign.common.handler;

import com.beside.mamgwanboo.common.util.ProtocolBufferUtil;
import com.beside.mamgwanboo.sign.common.service.SignServiceProxy;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.sign.SignRequest;
import reactor.core.publisher.Mono;

@Component
public class SignHandler {
  private final SignServiceProxy signServiceProxy;

  public SignHandler(SignServiceProxy signServiceProxy) {
    this.signServiceProxy = signServiceProxy;
  }

  public Mono<ServerResponse> sign(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(String.class)
        .flatMap(body -> ProtocolBufferUtil.<SignRequest>parse(body, SignRequest.newBuilder()))
        .flatMap(signServiceProxy::sign)
        .flatMap(signResponse -> ServerResponse
            .ok()
            .cookie(ResponseCookie
                .from("userJwt", signResponse.getJwt())
                .build())
            .bodyValue(ProtocolBufferUtil.print(signResponse)));
  }
}

