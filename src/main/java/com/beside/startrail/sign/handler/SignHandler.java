package com.beside.startrail.sign.handler;

import com.beside.startrail.common.service.JwtService;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.common.util.ProtocolBufferUtil;
import com.beside.startrail.user.repository.UserRepository;
import com.beside.startrail.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.sign.JwtPayload;
import protobuf.sign.SignRequest;
import reactor.core.publisher.Mono;

@Component
public class SignHandler implements HandlerFunction<ServerResponse> {
  private final UserRepository userRepository;
  private final JwtService jwtService;

  public SignHandler(UserRepository userRepository, JwtService jwtService) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
  }

  @Override
  public @NonNull Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest
        .bodyToMono(String.class)
        .flatMap(body ->
            ProtocolBufferUtil.<SignRequest>parse(
                body,
                SignRequest.newBuilder()
            )
        )
        .map(SignRequest::getSequence)
        .flatMap(sequence ->
            UserService
                .existsUser(sequence, YnType.Y)
                .execute(userRepository)
                .flatMap(isExists -> {
                  if (!isExists) {
                    return ServerResponse
                        .status(HttpStatus.UNAUTHORIZED)
                        .build();
                  }

                  return jwtService.makeJwt(
                          JwtPayload.newBuilder()
                              .setSequence(sequence)
                              .build()
                      )
                      .flatMap(jwt ->
                          ServerResponse
                              .ok()
                              .bodyValue(jwt)
                      );
                })
        )
        .onErrorResume(throwable -> ServerResponse
            .status(HttpStatus.UNAUTHORIZED)
            .build());
  }
}
