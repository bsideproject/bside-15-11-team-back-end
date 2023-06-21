package com.beside.startrail.user.document;

import com.beside.startrail.common.type.YnType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import protobuf.common.LevelInformation;
import protobuf.common.UserInformation;

@Builder
@Getter
@Document("user")
@EqualsAndHashCode
public class User {
  @Id
  UserId userId;
  private final String sequence;
  private final UserInformation userInformation;
  private final LevelInformation levelInformation;
  private final YnType useYn;
}
