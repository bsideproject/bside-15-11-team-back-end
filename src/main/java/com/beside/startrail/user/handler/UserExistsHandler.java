package com.beside.startrail.user.handler;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.command.UserExistsByUserIdAndUseYnCommand;
import com.beside.startrail.user.document.UserId;
import com.beside.startrail.user.repository.UserRepository;
import com.beside.startrail.user.type.OauthServiceType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserExistsHandler implements HandlerFunction<ServerResponse> {
  private final UserRepository userRepository;

  public UserExistsHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @NonNull
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    OauthServiceType oauthServiceType = OauthServiceType.valueOf(
        serverRequest.pathVariable("oauthServiceType")
    );
    String serviceUserId = serverRequest.pathVariable("serviceUserId");

    return Mono.just(
            UserId.builder()
                .oauthServiceType(oauthServiceType)
                .serviceUserId(serviceUserId)
                .build()
        )
        .map(userId -> new UserExistsByUserIdAndUseYnCommand(userId, YnType.Y))
        .flatMap(userExistsByUserIdAndUseYnCommand ->
            userExistsByUserIdAndUseYnCommand.execute(userRepository)
        )
        .flatMap(result ->
            ServerResponse
                .ok()
                .bodyValue(result)
        );
  }
}
