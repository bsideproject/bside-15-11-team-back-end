package com.beside.startrail.relationship.document;

import com.beside.startrail.common.type.YnType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@Document(collection = "relationship")
public class Relationship {
  @Id
  private final String sequence;
  private final String userSequence;

  private final String nickname;
  private final String relationship;
  private final Birth birth;
  private final String memo;

  @Builder.Default
  private final YnType useYn = YnType.Y;

  @CreatedDate
  private final LocalDateTime createDate;
  @LastModifiedDate
  private final LocalDateTime modifyDate;

  public static Relationship from(Relationship relationship, YnType useYn) {
    return Relationship.builder()
        .sequence(relationship.sequence)
        .userSequence(relationship.userSequence)
        .nickname(relationship.nickname)
        .relationship(relationship.relationship)
        .birth(relationship.birth)
        .memo(relationship.memo)
        .useYn(useYn)
        .build();
  }

  public static Relationship from(Relationship relationship, String userSequence) {
    return Relationship.builder()
        .sequence(relationship.sequence)
        .userSequence(userSequence)
        .nickname(relationship.nickname)
        .relationship(relationship.relationship)
        .birth(relationship.birth)
        .memo(relationship.memo)
        .useYn(relationship.getUseYn())
        .build();
  }
}
