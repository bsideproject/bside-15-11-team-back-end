package com.beside.startrail.user.handler;

import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.common.UserInformationProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.repository.UserRepository;
import com.beside.startrail.user.service.UserService;
import com.beside.startrail.user.type.OauthServiceType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.common.type.OauthServiceTypeProto;
import protobuf.user.UserGetRequestProto;
import protobuf.user.UserResponseProto;
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
        .<UserGetRequestProto>from(serverRequest.queryParams(), UserGetRequestProto.newBuilder())
        .map(userGetRequestProto ->
            UserService.getUser(
                OauthServiceType.valueOf(userGetRequestProto.getOauthServiceType().name()),
                userGetRequestProto.getServiceUserId(),
                YnType.Y
            )
        )
        .flatMap(userFindCommand ->
            userFindCommand.execute(userRepository)
        )
        .map(this::toUserResponseProto)
        .flatMap(userResponseProto ->
            ServerResponse
                .ok()
                .bodyValue(ProtocolBufferUtil.print(userResponseProto))
        )
        .switchIfEmpty(
            ServerResponse
                .noContent()
                .build()
        );
  }


  private UserResponseProto toUserResponseProto(User user) {
    return UserResponseProto.newBuilder()
        .setSequence(String.valueOf(user.getSequence()))
        .setOauthServiceType(
            OauthServiceTypeProto.valueOf(user.getUserId().getOauthServiceType().name())
        )
        .setUserInformation(
            UserInformationProtoUtil.toUserInformationProto(user.getUserInformation())
        )
        .build();
  }
}
