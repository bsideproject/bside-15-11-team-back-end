package com.beside.startrail.user.handler;

import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.user.UserIdProtoUtil;
import com.beside.startrail.common.protocolbuffer.user.UserProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.document.UserId;
import com.beside.startrail.user.repository.UserRepository;
import com.beside.startrail.user.service.UserService;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.user.UserPostRequestProto;
import reactor.core.publisher.Mono;

@Component
public class UserPostHandler implements HandlerFunction<ServerResponse> {
  private final UserRepository userRepository;

  public UserPostHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @NonNull
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest
        .bodyToMono(String.class)
        .flatMap(body ->
            ProtocolBufferUtil.<UserPostRequestProto>parse(body, UserPostRequestProto.newBuilder())
        )
        .flatMap(userPostRequestProto -> {
          UserId userId = UserIdProtoUtil.toUserId(
              userPostRequestProto.getUserId()
          );

          return Mono
              .just(UserService.existsByUserIdAndUseYn(userId, YnType.Y))
              .flatMap(userExistsByUserIdAndUseYnCommand ->
                  userExistsByUserIdAndUseYnCommand.execute(userRepository)
              )
              .map(result -> !result)
              .filter(Boolean::booleanValue)
              .mapNotNull(__ -> UserProtoUtil.toUser(userPostRequestProto))
              .<User>map(user -> User.fromSequence(user, UUID.randomUUID().toString()))
              .map(UserService::create)
              .flatMap(userSaveCommand -> userSaveCommand.execute(userRepository))
              .map(UserProtoUtil::toUserResponseProto)
              .flatMap(userResponseProto ->
                  ServerResponse
                      .ok()
                      .contentType(MediaType.APPLICATION_JSON)
                      .bodyValue(ProtocolBufferUtil.print(userResponseProto))
              )
              .switchIfEmpty(
                  ServerResponse
                      .status(HttpStatus.CONFLICT)
                      .build()
              );
        });
  }
}
