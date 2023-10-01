package com.beside.startrail.sign.handler;

import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.user.UserIdProtoUtil;
import com.beside.startrail.common.service.JwtProtoService;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.repository.UserRepository;
import com.beside.startrail.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.sign.JwtPayloadProto;
import protobuf.sign.SignRequestProto;
import reactor.core.publisher.Mono;

@Component
public class SignHandler implements HandlerFunction<ServerResponse> {
  private final UserRepository userRepository;
  private final JwtProtoService jwtProtoService;

  public SignHandler(UserRepository userRepository, JwtProtoService jwtProtoService) {
    this.userRepository = userRepository;
    this.jwtProtoService = jwtProtoService;
  }

  @Override
  @NonNull
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest
        .bodyToMono(String.class)
        .flatMap(body ->
            ProtocolBufferUtil.<SignRequestProto>parse(body, SignRequestProto.newBuilder())
        )
        .mapNotNull(signRequestProto ->
            UserIdProtoUtil.toUserId(signRequestProto.getUserId())
        )
        .map(userId ->
            UserService.getByUserId(userId, YnType.Y)
        )
        .flatMap(command ->
            command.execute(userRepository)
        )
        .map(user ->
            JwtPayloadProto.newBuilder()
                .setSequence(user.getSequence())
                .build()
        )
        .flatMap(jwtPayloadProto ->
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    BodyInserters.fromProducer(
                        jwtProtoService.makeJwtProto(jwtPayloadProto),
                        String.class
                    )
                )
        )
        .switchIfEmpty(
            ServerResponse
                .status(HttpStatus.UNAUTHORIZED)
                .build()
        );
  }
}
