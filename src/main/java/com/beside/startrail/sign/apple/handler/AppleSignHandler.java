package com.beside.startrail.sign.apple.handler;

import com.beside.startrail.common.service.JwtProtoService;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.sign.apple.service.AppleSignService;
import com.beside.startrail.user.command.UserSaveCommand;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.document.UserId;
import com.beside.startrail.user.model.AllowInformation;
import com.beside.startrail.user.model.UserInformation;
import com.beside.startrail.user.repository.UserRepository;
import com.beside.startrail.user.type.OauthServiceType;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.sign.JwtPayloadProto;
import reactor.core.publisher.Mono;

@Component
public class AppleSignHandler implements HandlerFunction<ServerResponse> {
  private final AppleSignService appleSignService;
  private final UserRepository userRepository;
  private final JwtProtoService jwtProtoService;

  public AppleSignHandler(
      AppleSignService appleSignService,
      UserRepository userRepository,
      JwtProtoService jwtProtoService
  ) {
    this.appleSignService = appleSignService;
    this.userRepository = userRepository;
    this.jwtProtoService = jwtProtoService;
  }

  @Override
  @NonNull
  public Mono<ServerResponse> handle(ServerRequest request) {
    return request
        .formData()
        .mapNotNull(multiValueMap -> multiValueMap.getFirst("id_token"))
        .map(idToken -> {
              try {
                return appleSignService.getPayload(idToken);
              } catch (IOException ioException) {
                throw new IllegalArgumentException(
                    String.format("payload를 풀어내지 못했습니다. idToken: %s", idToken));
              }
            }
        )
        .filter(appleIdToken -> Objects.nonNull(appleIdToken.getEmail()))
        .map(appleIdToken ->
            User.builder()
                .userId(
                    UserId.builder()
                        .oauthServiceType(OauthServiceType.APPLE)
                        .serviceUserId(appleIdToken.getEmail())
                        .build()
                )
                .sequence(UUID.randomUUID().toString())
                .userInformation(
                    UserInformation.builder()
                        .profileNickname(appleIdToken.getEmail())
                        .build()
                )
                .useYn(YnType.Y)
                .allowInformation(
                    AllowInformation.builder()
                        .serviceYn(YnType.N)
                        .privateInformationYn(YnType.N)
                        .eventMarketingYn(YnType.N)
                        .build()
                )
                .build()
        )
        .flatMap(user ->
            userRepository.findUserByUserIdAndUseYn(
                    user.getUserId(),
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
        )
        .switchIfEmpty(
            ServerResponse
                .status(503)
                .build()
        );
  }
}
