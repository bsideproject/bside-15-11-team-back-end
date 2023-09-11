package com.beside.startrail.user.document;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.model.AllowInformation;
import com.beside.startrail.user.model.UserInformation;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@Document("user")
@EqualsAndHashCode
public class User {
  @Id
  private final UserId userId;
  private final String sequence;
  private final UserInformation userInformation;
  @Builder.Default
  private final YnType useYn = YnType.Y;
  @CreatedDate
  private final LocalDateTime createDate;
  @LastModifiedDate
  private final LocalDateTime modifyDate;
  private final AllowInformation allowInformation;
  private final String withdrawlReason;

  public static User fromUseYn(User user, YnType useYn) {
    return User.builder()
        .userId(user.userId)
        .sequence(user.sequence)
        .userInformation(user.userInformation)
        .allowInformation(user.allowInformation)
        .useYn(useYn)
        .withdrawlReason(user.getWithdrawlReason())
        .build();
  }

  public static User fromAllowInformation(User user, AllowInformation allowInformation) {
    return User.builder()
        .userId(user.userId)
        .sequence(user.sequence)
        .userInformation(user.userInformation)
        .allowInformation(allowInformation)
        .useYn(user.useYn)
        .withdrawlReason(user.getWithdrawlReason())
        .build();
  }

  public static User fromReason(User user, String reason) {
    return User.builder()
        .userId(user.getUserId())
        .sequence(user.getSequence())
        .userInformation(user.getUserInformation())
        .allowInformation(user.getAllowInformation())
        .useYn(user.getUseYn())
        .withdrawlReason(reason)
        .build();
  }

  public static User fromSequence(User user, String sequence) {
    return User.builder()
        .userId(user.userId)
        .sequence(sequence)
        .userInformation(user.userInformation)
        .allowInformation(user.allowInformation)
        .useYn(user.getUseYn())
        .withdrawlReason(user.getWithdrawlReason())
        .build();
  }
}
