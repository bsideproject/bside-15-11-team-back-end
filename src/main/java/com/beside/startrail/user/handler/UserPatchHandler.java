package com.beside.startrail.user.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.user.UserProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.repository.UserRepository;
import com.beside.startrail.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.user.UserPatchRequestProto;
import reactor.core.publisher.Mono;

@Component
public class UserPatchHandler extends AbstractSignedTransactionalHandler {
  private final UserRepository userRepository;

  public UserPatchHandler(
      @Value("${sign.attributeName}") String attributeName,
      UserRepository userRepository
  ) {
    super(attributeName);
    this.userRepository = userRepository;
  }

  @Override
  protected Mono<ServerResponse> signedTransactionalHandle(ServerRequest serverRequest) {
    String sequence = jwtPayloadProto.getSequence();

    return UserService
        .getBySequence(sequence, YnType.Y)
        .execute(userRepository)
        .flatMap(user ->
            serverRequest
                .bodyToMono(String.class)
                .flatMap(body ->
                    ProtocolBufferUtil.<UserPatchRequestProto>parse(
                        body,
                        UserPatchRequestProto.newBuilder()
                    )
                )
                .mapNotNull(UserProtoUtil::toUser)
                .map(editUser -> mergeUser(user, editUser))
                .map(UserService::create
                )
                .flatMap(userSaveCommand ->
                    userSaveCommand.execute(userRepository)
                )
                .map(UserProtoUtil::toUserResponseProto)
                .flatMap(userResponseProto ->
                    ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ProtocolBufferUtil.print(userResponseProto))
                )
                .switchIfEmpty(
                    ServerResponse
                        .badRequest()
                        .build()
                )
        );
  }

  private User mergeUser(User originUser, User editUser) {
    return User.builder()
        .userId(originUser.getUserId())
        .sequence(originUser.getSequence())
        .userInformation(editUser.getUserInformation())
        .useYn(originUser.getUseYn())
        .allowInformation(editUser.getAllowInformation())
        .build();
  }
}
