package com.beside.startrail.user.sheculed;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.command.UserFindCommand;
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

    new UserFindCommand(
        YnType.N,
        YnType.Y,
        now.minusDays(14),
        now
    )
        .execute(userRepository)
        .subscribe(user -> user.setUseYn(YnType.N));
  }
}
