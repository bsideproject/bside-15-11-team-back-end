package com.beside.startrail.user.document;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.model.UserInformation;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Setter
@Getter
@Document("user")
@EqualsAndHashCode
public class User {
  @Id
  private UserId userId;
  private String sequence;
  private UserInformation userInformation;
  @Builder.Default
  private YnType useYn = YnType.Y;
  @CreatedDate
  private LocalDateTime createdDate;
  @LastModifiedDate
  private LocalDateTime modifiedDate;
  @Builder.Default
  private YnType allowPrivateInformationYn = YnType.Y;
}
