package com.beside.startrail.friend.document;

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
@Document(collection = "friend")
public class Friend {
    @Id
    private String sequence;
    private String userSequence;

    private String nickname;
    private String relationship;
    private Birth birth;
    private String memo;

    @Builder.Default
    private YnType useYn = YnType.Y;

    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public Friend notUseYn(Friend friend){
        friend.useYn = YnType.N;
        return friend;
    }
}
