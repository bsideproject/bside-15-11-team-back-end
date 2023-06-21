package com.beside.startrail.user.handler;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.common.util.ProtocolBufferUtil;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.document.UserId;
import com.beside.startrail.user.repository.UserRepository;
import com.beside.startrail.user.service.UserService;
import java.util.UUID;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.user.UserPatchRequest;
import protobuf.user.UserResponse;
import reactor.core.publisher.Mono;

@Component
public class UserCreateHandler implements HandlerFunction<ServerResponse> {
  private final UserRepository userRepository;

  public UserCreateHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public @NonNull Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest
        .bodyToMono(String.class)
        .flatMap(body ->
            ProtocolBufferUtil.<UserPatchRequest>parse(
                body,
                UserPatchRequest.newBuilder()
            )
        )
        .map(this::makeUser)
        .map(UserService::createUser)
        .flatMap(userCreateCommand ->
            userCreateCommand.execute(userRepository)
        )
        .map(this::toUserResponse)
        .flatMap(userResponse ->
            ServerResponse
                .ok()
                .bodyValue(ProtocolBufferUtil.print(userResponse))
        );
  }

  private User makeUser(UserPatchRequest userPatchRequest) {
    return User.builder()
        .userId(
            UserId.builder()
                .oauthServiceType(userPatchRequest.getOauthServiceType())
                .serviceUserId(userPatchRequest.getServiceUserId())
                .build()
        )
        .sequence(UUID.randomUUID().toString())
        .userInformation(userPatchRequest.getUserInformation())
        .useYn(YnType.Y)
        .build();
  }

  private UserResponse toUserResponse(User user) {
    return UserResponse.newBuilder()
        .setSequence(String.valueOf(user.getSequence()))
        .setOauthServiceType(user.getUserId().getOauthServiceType())
        .setUserInformation(user.getUserInformation())
        .build();
  }
}
