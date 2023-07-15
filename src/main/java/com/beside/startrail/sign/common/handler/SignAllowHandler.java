package com.beside.startrail.sign.common.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.command.UserFindBySequenceCommand;
import com.beside.startrail.user.command.UserSaveCommand;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class SignAllowHandler extends AbstractSignedTransactionalHandler {
  private final UserRepository userRepository;

  public SignAllowHandler(
      @Value("${sign.attributeName}") String attributeName,
      UserRepository userRepository
  ) {
    super(attributeName);
    this.userRepository = userRepository;
  }

  @Override
  protected Mono<ServerResponse> signedTransactionalHandle(ServerRequest serverRequest) {
    String sequence = super.jwtPayloadProto.getSequence();

    return new UserFindBySequenceCommand(sequence)
        .execute(userRepository)
        .map(user -> new UserSaveCommand(User.fromAllowPrivateInformationYn(user, YnType.Y)))
        .flatMap(userSaveCommand -> userSaveCommand.execute(userRepository))
        .flatMap(__ ->
            ServerResponse
                .ok()
                .build()
        )
        .switchIfEmpty(
            ServerResponse
                .badRequest()
                .build()
        );
  }
}
