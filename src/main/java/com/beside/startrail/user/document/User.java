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

  public static User fromUseYn(User user, YnType useYn) {
    return User.builder()
        .userId(user.userId)
        .sequence(user.sequence)
        .userInformation(user.userInformation)
        .allowInformation(
            AllowInformation.builder()
                .serviceYn(user.allowInformation.getServiceYn())
                .privateInformationYn(user.allowInformation.getPrivateInformationYn())
                .eventMarketingYn(user.allowInformation.getEventMarketingYn())
                .build()
        )
        .useYn(useYn)
        .build();
  }
}
