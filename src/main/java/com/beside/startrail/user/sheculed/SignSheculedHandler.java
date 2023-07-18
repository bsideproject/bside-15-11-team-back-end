package com.beside.startrail.user.sheculed;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.command.UserFindByServiceYnAndPrivateInformationYnCommand;
import com.beside.startrail.user.command.UserSaveCommand;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.repository.UserRepository;
import java.time.LocalDate;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@EnableScheduling
@Configuration
public class SignSheculedHandler {
  private final UserRepository userRepository;

  public SignSheculedHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  @Scheduled(cron = "0 0 0 ? * MON")
  public void deleteUnAllowedUsers() {
    LocalDate now = LocalDate.now();

    new UserFindByServiceYnAndPrivateInformationYnCommand(
        YnType.N,
        YnType.N,
        YnType.Y,
        now.minusDays(14),
        now
    )
        .execute(userRepository)
        .map(user -> new UserSaveCommand(User.fromUseYn(user, YnType.N)))
        .flatMap(userSaveCommand -> userSaveCommand.execute(userRepository));
  }
}
