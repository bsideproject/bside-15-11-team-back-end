package com.beside.startrail.mind.document;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.type.MindType;
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
@Document("mind")
@EqualsAndHashCode
public class Mind {
  @Id
  private final String sequence;
  private final String userSequence;
  private final String relationshipSequence;
  private final MindType type;
  private final String event;
  private final LocalDateTime date;
  @CreatedDate
  private final LocalDateTime createDate;
  @LastModifiedDate
  private final LocalDateTime modifyDate;
  private final Item item;
  private final String memo;
  private final YnType useYn;

  public static Mind from(Mind relationship, YnType useYn) {
    return Mind.builder()
        .sequence(relationship.sequence)
        .userSequence(relationship.userSequence)
        .relationshipSequence(relationship.relationshipSequence)
        .type(relationship.type)
        .event(relationship.event)
        .date(relationship.date)
        .item(relationship.item)
        .memo(relationship.memo)
        .useYn(useYn)
        .build();
  }
}
