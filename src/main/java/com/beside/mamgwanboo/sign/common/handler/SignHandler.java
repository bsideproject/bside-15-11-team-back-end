package com.beside.mamgwanboo.sign.common.handler;

import com.beside.mamgwanboo.common.service.JwtService;
import com.beside.mamgwanboo.common.util.ProtocolBufferUtil;
import com.beside.mamgwanboo.sign.common.service.SignService;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.sign.MamgwanbooJwtPayload;
import protobuf.sign.SignRequest;
import protobuf.sign.SignResponse;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@Component
public class SignHandler implements HandlerFunction<ServerResponse> {
  private final SignService signService;
  private final JwtService jwtService;


  public SignHandler(SignService signService, JwtService jwtService) {
    this.signService = signService;
    this.jwtService = jwtService;
  }

  @Override
  @NonNull
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(String.class)
        .flatMap(body -> ProtocolBufferUtil.<SignRequest>parse(body, SignRequest.newBuilder()))
        .flatMap(signRequest ->
            signService.sign(
                signRequest.getOauthServiceType(),
                signRequest.getCode()
            )
        )
        .flatMap(signResult ->
            jwtService.makeJwt(MamgwanbooJwtPayload.newBuilder()
                    .setSequence(signResult.getUser().getSequence())
                    .build())
                .map(mamgwanbooJwt ->
                    SignResponse.newBuilder()
                        .setJwt(mamgwanbooJwt)
                        .setIsNewUser(signResult.isNewUser())
                        .build()))
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

