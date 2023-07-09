package com.beside.startrail.sign.common.handler;

import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.service.JwtProtoService;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.sign.common.command.AbstractSignCommand;
import com.beside.startrail.user.document.UserId;
import com.beside.startrail.user.repository.UserRepository;
import com.beside.startrail.user.type.OauthServiceType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.sign.JwtPayloadProto;
import protobuf.sign.SignRequestProto;
import reactor.core.publisher.Mono;

@Component
public class SignHandler {
  private final UserRepository userRepository;
  private final JwtProtoService jwtProtoService;

  public SignHandler(UserRepository userRepository, JwtProtoService jwtProtoService) {
    this.userRepository = userRepository;
    this.jwtProtoService = jwtProtoService;
  }

  public Mono<ServerResponse> sign(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(String.class)
        .flatMap(
            body -> ProtocolBufferUtil.<SignRequestProto>parse(body, SignRequestProto.newBuilder()))
        .map(signRequestProto ->
            Enum.valueOf(
                OauthServiceType.class,
                signRequestProto.getOauthServiceType().name()
            ).getSignCommand(signRequestProto.getCode(), signRequestProto.getClientSecret())
        )
        .flatMap(AbstractSignCommand::execute)
        .flatMap(user ->
            userRepository.findUserByUserIdAndUseYn(
                    UserId.builder()
                        .oauthServiceType(user.getUserId().getOauthServiceType())
                        .serviceUserId(user.getUserId().getServiceUserId())
                        .build(),
                    YnType.Y
                )
                .switchIfEmpty(userRepository.insert(user))
        )
        .flatMap(user ->
            jwtProtoService.makeJwtProto(
                JwtPayloadProto.newBuilder()
                    .setSequence(user.getSequence())
                    .build()
            )
        )
        .flatMap(jwt -> ServerResponse
            .ok()
            .bodyValue(jwt)
        );
  }
}

