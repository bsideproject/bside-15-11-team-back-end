package com.beside.mamgwanboo.sign.common.command;

import java.util.List;
import org.springframework.stereotype.Component;
import protobuf.common.type.OauthServiceType;

@Component
public class SignCommandProxy {
  private final List<SignCommand> signCommands;

  public SignCommandProxy(List<SignCommand> signCommands) {
    this.signCommands = signCommands;
  }

  public SignCommand getTargetCommand(OauthServiceType oauthServiceType) {
    return signCommands.stream()
        .filter(signCommand -> signCommand.isTargetService(oauthServiceType))
        .findFirst()
        .orElse(null);
  }
}
