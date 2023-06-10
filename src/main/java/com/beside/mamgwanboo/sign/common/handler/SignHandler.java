package com.beside.mamgwanboo.sign.common.handler;

import com.beside.mamgwanboo.common.service.JwtService;
import com.beside.mamgwanboo.common.util.ProtocolBufferUtil;
import com.beside.mamgwanboo.sign.common.service.SignService;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.sign.MamgwanbooJwtPayload;
import protobuf.sign.SignRequest;
import protobuf.sign.SignResponse;
import reactor.core.publisher.Mono;

@Component
public class SignHandler {
  private final SignService signService;
  private final JwtService jwtService;


  public SignHandler(SignService signService, JwtService jwtService) {
    this.signService = signService;
    this.jwtService = jwtService;
  }

  public Mono<ServerResponse> sign(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(String.class)
        .flatMap(body -> ProtocolBufferUtil.<SignRequest>parse(body, SignRequest.newBuilder()))
        .flatMap(signRequest ->
            signService.sign(
                signRequest.getOauthServiceType(),
                signRequest.getCode()
            )
        )
        .map(signResult ->
            SignResponse.newBuilder()
                .setJwt(jwtService.makeMamgwanbooJwt(MamgwanbooJwtPayload.newBuilder()
                    .setSequence(signResult.getUser().getSequence())
                    .build()))
                .setIsNewUser(signResult.isNewUser())
                .build()
        )
        .flatMap(signResponse ->
            ServerResponse
                .ok()
                .cookie(ResponseCookie
                    .from("jwt", signResponse.getJwt())
                    .build())
                .bodyValue(ProtocolBufferUtil.print(signResponse))
        );
  }
}

