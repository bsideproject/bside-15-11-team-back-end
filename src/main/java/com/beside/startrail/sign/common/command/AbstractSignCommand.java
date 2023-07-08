package com.beside.startrail.sign.common.command;

import com.beside.startrail.user.document.User;
import com.beside.startrail.user.type.OauthServiceType;
import reactor.core.publisher.Mono;

public abstract class AbstractSignCommand {
  private final OauthServiceType oauthServiceType;
  protected final String code;
  private Mono<User> result;

  protected AbstractSignCommand(OauthServiceType oauthServiceType, String code) {
    this.oauthServiceType = oauthServiceType;
    this.code = code;
  }

  public Mono<User> execute() {
    result = makeUser();

    return result;
  }

  protected abstract Mono<User> makeUser();
}
