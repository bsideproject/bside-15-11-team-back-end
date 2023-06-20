package com.beside.mamgwanboo.relationship.document;

import com.beside.mamgwanboo.common.type.YnType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import protobuf.common.type.RelationshipType;

@Builder
@Getter
@Document("relationship")
@EqualsAndHashCode
public class Relationship {
  @Id
  private final String sequence;
  private final String userSequence;
  private final String friendSequence;
  private final RelationshipType type;
  private final String event;
  private final LocalDateTime date;
  @CreatedDate
  private final LocalDateTime createDate;
  @LastModifiedDate
  private final LocalDateTime modifyDate;
  private final Item item;
  private final String memo;
  private final YnType useYn;
}
