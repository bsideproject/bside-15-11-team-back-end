package com.beside.startrail.user.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.repository.UserRepository;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import reactor.core.publisher.Mono;

@Getter
@EqualsAndHashCode
public class UserExistsCommand {
  private final String sequence;
  private final YnType useYn;
  private Mono<Boolean> result;

  public UserExistsCommand(String sequence, YnType useYn) {
    this.sequence = sequence;
    this.useYn = useYn;
  }

  public Mono<Boolean> execute(UserRepository userRepository) {
    result = userRepository.existsBySequenceAndUseYn(sequence, useYn);

    return result;
  }
}
