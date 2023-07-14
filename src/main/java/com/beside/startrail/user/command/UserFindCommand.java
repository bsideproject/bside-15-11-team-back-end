package com.beside.startrail.user.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.repository.UserRepository;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import reactor.core.publisher.Flux;

@EqualsAndHashCode
@Getter
public class UserFindCommand {
  private final YnType allowPrivateInformationYn;
  private final YnType useYn;
  private final LocalDate from;
  private final LocalDate to;
  private Flux<User> result;

  public UserFindCommand(
      YnType allowPrivateInformationYn,
      YnType useYn,
      LocalDate from,
      LocalDate to
  ) {
    this.allowPrivateInformationYn = allowPrivateInformationYn;
    this.useYn = useYn;
    this.from = from;
    this.to = to;
  }

  public Flux<User> execute(UserRepository userRepository) {
    result = userRepository.findByAllowPrivateInformationYnAndUseYnAndModifiedDateIsBetween(
        allowPrivateInformationYn,
        useYn,
        from.atStartOfDay(),
        to.atStartOfDay()
    );

    return result;
  }
}
