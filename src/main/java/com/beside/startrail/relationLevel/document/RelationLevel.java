package com.beside.startrail.relationLevel.document;

import com.beside.startrail.common.type.YnType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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

    public void notUse(){
        this.useYn = YnType.N;
    }
}
