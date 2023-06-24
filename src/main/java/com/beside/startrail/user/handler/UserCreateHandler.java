package com.beside.startrail.user.handler;

import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.common.UserInformationProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.document.UserId;
import com.beside.startrail.user.repository.UserRepository;
import com.beside.startrail.user.service.UserService;
import com.beside.startrail.user.type.OauthServiceType;
import java.util.UUID;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.common.type.OauthServiceTypeProto;
import protobuf.user.UserPatchRequestProto;
import protobuf.user.UserResponseProto;
import reactor.core.publisher.Mono;

@Component
public class UserCreateHandler implements HandlerFunction<ServerResponse> {
  private final UserRepository userRepository;

  public UserCreateHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  @Override
  public @NonNull Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest
        .bodyToMono(String.class)
        .flatMap(body ->
            ProtocolBufferUtil.<UserPatchRequestProto>parse(
                body,
                UserPatchRequestProto.newBuilder()
            )
        )
        .map(this::makeUser)
        .map(UserService::createUser)
        .flatMap(userCreateCommand ->
            userCreateCommand.execute(userRepository)
        )
        .map(this::toUserResponseProto)
        .flatMap(userResponseProto ->
            ServerResponse
                .ok()
                .bodyValue(ProtocolBufferUtil.print(userResponseProto))
        );
  }

  private User makeUser(UserPatchRequestProto userPatchRequest) {
    return User.builder()
        .userId(
            UserId.builder()
                .oauthServiceType(
                    OauthServiceType.valueOf(
                        userPatchRequest.getOauthServiceType().name()
                    )
                )
                .serviceUserId(userPatchRequest.getServiceUserId())
                .build()
        )
        .sequence(UUID.randomUUID().toString())
        .userInformation(
            UserInformationProtoUtil.toUserInformation(userPatchRequest.getUserInformation())
        )
        .useYn(YnType.Y)
        .build();
  }

  private UserResponseProto toUserResponseProto(User user) {
    return UserResponseProto.newBuilder()
        .setSequence(String.valueOf(user.getSequence()))
        .setOauthServiceType(
            OauthServiceTypeProto.valueOf(
                user.getUserId().getOauthServiceType().name()
            )
        )
        .setUserInformation(
            UserInformationProtoUtil.toUserInformationProto(user.getUserInformation())
        )
        .build();
  }
}
