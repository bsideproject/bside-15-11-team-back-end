package com.beside.startrail.user.document;

import com.beside.startrail.common.type.YnType;
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
  private final LocalDateTime createdDate;
  @LastModifiedDate
  private final LocalDateTime modifiedDate;
  @Builder.Default
  private final YnType allowPrivateInformationYn = YnType.N;

  public static User fromUseYn(User user, YnType useYn) {
    return User.builder()
        .userId(user.userId)
        .sequence(user.sequence)
        .userInformation(user.userInformation)
        .allowPrivateInformationYn(user.allowPrivateInformationYn)
        .useYn(useYn)
        .build();
  }

  public static User fromAllowPrivateInformationYn(User user, YnType allowPrivateInformationYn) {
    return User.builder()
        .userId(user.userId)
        .sequence(user.sequence)
        .userInformation(user.userInformation)
        .allowPrivateInformationYn(allowPrivateInformationYn)
        .useYn(user.useYn)
        .build();
  }
}
