package com.beside.startrail.user.document;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.model.UserInformation;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@Document("user")
@EqualsAndHashCode
public class User {
  @Id
  UserId userId;
  private final String sequence;
  private final UserInformation userInformation;
  private final YnType useYn;
}
