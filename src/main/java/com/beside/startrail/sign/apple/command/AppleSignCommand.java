package com.beside.startrail.sign.apple.command;

import com.beside.startrail.sign.common.command.AbstractSignCommand;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.type.OauthServiceType;
import reactor.core.publisher.Mono;

public class AppleSignCommand extends AbstractSignCommand {
  public AppleSignCommand(String code) {
    super(OauthServiceType.APPLE, code);
  }
  @Override
  protected Mono<User> makeUser() {
    // todo
    return null;
  }
}
