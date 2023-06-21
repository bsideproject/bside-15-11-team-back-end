package com.beside.startrail.user.handler;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.common.util.ProtocolBufferUtil;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.repository.UserRepository;
import com.beside.startrail.user.service.UserService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.user.UserGetRequest;
import protobuf.user.UserResponse;
import reactor.core.publisher.Mono;

@Component
public class UserGetHandler implements HandlerFunction<ServerResponse> {
  private final UserRepository userRepository;

  public UserGetHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public @NonNull Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return ProtocolBufferUtil
        .<UserGetRequest>from(serverRequest.queryParams(), UserGetRequest.newBuilder())
        .map(userGetRequest ->
            UserService.getUser(
                userGetRequest.getOauthServiceType(),
                userGetRequest.getServiceUserId(),
                YnType.Y
            )
        )
        .flatMap(userFindCommand ->
            userFindCommand.execute(userRepository)
        )
        .map(this::toUserResponse)
        .flatMap(userResponse ->
            ServerResponse
                .ok()
                .bodyValue(ProtocolBufferUtil.print(userResponse))
        )
        .switchIfEmpty(
            ServerResponse
                .noContent()
                .build()
        );
  }


  private UserResponse toUserResponse(User user) {
    return UserResponse.newBuilder()
        .setSequence(String.valueOf(user.getSequence()))
        .setOauthServiceType(user.getUserId().getOauthServiceType())
        .setUserInformation(user.getUserInformation())
        .build();
  }
}
