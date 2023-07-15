package com.beside.startrail.sign.kakao.handler;

import com.beside.startrail.common.service.JwtProtoService;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.sign.kakao.command.KakaoSignCommand;
import com.beside.startrail.user.command.UserSaveCommand;
import com.beside.startrail.user.document.UserId;
import com.beside.startrail.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.sign.JwtPayloadProto;
import reactor.core.publisher.Mono;

@Component
public class KakaoSignHandler implements HandlerFunction<ServerResponse> {
  private final String accessTokenUri;
  private final String apiUri;
  private final String redirectUri;
  private final String clientId;
  private final UserRepository userRepository;
  private final JwtProtoService jwtProtoService;

  public KakaoSignHandler(
      @Value("${oauth.kakao.accessTokenUri}") String accessTokenUri,
      @Value("${oauth.kakao.apiUri}") String apiUri,
      @Value("${oauth.kakao.redirectUri}") String redirectUri,
      @Value("${oauth.kakao.clientId}") String clientId,
      UserRepository userRepository,
      JwtProtoService jwtProtoService
  ) {
    this.accessTokenUri = accessTokenUri;
    this.apiUri = apiUri;
    this.redirectUri = redirectUri;
    this.clientId = clientId;
    this.userRepository = userRepository;
    this.jwtProtoService = jwtProtoService;
  }

  @Override
  @NonNull
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest.queryParam("code")
        .map(s ->
            Mono.just(s)
                .map(code ->
                    new KakaoSignCommand(
                        accessTokenUri,
                        apiUri,
                        code,
                        redirectUri,
                        clientId
                    )
                )
                .flatMap(KakaoSignCommand::execute)
                .flatMap(user ->
                    userRepository.findUserByUserIdAndUseYn(
                            UserId.builder()
                                .oauthServiceType(user.getUserId().getOauthServiceType())
                                .serviceUserId(user.getUserId().getServiceUserId())
                                .build(),
                            YnType.Y
                        )
                        .switchIfEmpty(
                            new UserSaveCommand(user)
                                .execute(userRepository)
                        )
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
                ))
        .orElseGet(() ->
            ServerResponse
                .badRequest()
                .build()
        );

  }
}
