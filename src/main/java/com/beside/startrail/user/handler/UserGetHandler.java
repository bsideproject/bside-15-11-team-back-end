package com.beside.startrail.user.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.common.protocolbuffer.user.UserProtoUtil;
import com.beside.startrail.user.command.UserFindBySequenceCommand;
import com.beside.startrail.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserGetHandler extends AbstractSignedHandler {
  private final UserRepository userRepository;

  public UserGetHandler(
      @Value("${sign.attributeName}") String attributeName,
      UserRepository userRepository
  ) {
    super(attributeName);
    this.userRepository = userRepository;
  }

  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    return new UserFindBySequenceCommand(jwtPayloadProto.getSequence())
        .execute(userRepository)
        .map(UserProtoUtil::toUserResponseProto)
        .flatMap(userResponseProto ->
            ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ProtocolBufferUtil.print(userResponseProto))
        )
        .switchIfEmpty(
            ServerResponse
                .noContent()
                .build()
        );
  }
}
