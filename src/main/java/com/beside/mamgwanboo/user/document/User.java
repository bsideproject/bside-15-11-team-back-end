package com.beside.mamgwanboo.user.document;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.user.model.UserInformation;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import protobuf.common.LevelInformation;

@Builder
@Getter
@Document("user")
public class User {
  @Id
  private String sequence;
  private UserInformation userInformation;
  private LevelInformation levelInformation;
  private YnType useYn;
}
