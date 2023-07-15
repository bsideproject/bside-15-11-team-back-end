package com.beside.startrail.relationLevel.document;

import com.beside.startrail.common.type.YnType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "relationLevel")
public class RelationLevel {
    @Id
    private String sequence;

    private String title;
    private String description;
    private Integer level;
    private Integer countFrom;
    private Integer countTo;

    @Builder.Default
    private YnType useYn = YnType.Y;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    public static RelationLevel makeDefault() {
        return RelationLevel.builder()
            .level(-1)
            .title("알 수 없는 블랙홀")
            .description("알 수 없는 친구")
            .build();
    }
}
