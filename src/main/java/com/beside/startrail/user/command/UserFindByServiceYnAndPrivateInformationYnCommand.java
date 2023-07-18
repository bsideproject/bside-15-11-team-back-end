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
public class UserFindByServiceYnAndPrivateInformationYnCommand {
  private final YnType serviceYn;
  private final YnType privateInformationYn;
  private final YnType useYn;
  private final LocalDate from;
  private final LocalDate to;
  private Flux<User> result;

  public UserFindByServiceYnAndPrivateInformationYnCommand(
      YnType serviceYn,
      YnType privateInformationYn,
      YnType useYn,
      LocalDate from,
      LocalDate to
  ) {
    this.serviceYn = serviceYn;
    this.privateInformationYn = privateInformationYn;
    this.useYn = useYn;
    this.from = from;
    this.to = to;
  }

  public Flux<User> execute(UserRepository userRepository) {
    result =
        userRepository.findByAllowInformation_ServiceYnAndAllowInformation_PrivateInformationYnAndUseYn(
            serviceYn,
            privateInformationYn,
            useYn,
            from.atStartOfDay(),
            to.atStartOfDay()
        );

    return result;
  }
}
