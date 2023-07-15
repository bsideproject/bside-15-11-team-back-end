package com.beside.startrail.friend.document;

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
@Document(collection = "friend")
public class Friend {
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
    private final LocalDateTime createdDate;
    @LastModifiedDate
    private final LocalDateTime modifiedDate;

    public static Friend from(Friend friend, YnType useYn){
        return Friend.builder()
            .sequence(friend.sequence)
            .userSequence(friend.userSequence)
            .nickname(friend.nickname)
            .relationship(friend.relationship)
            .birth(friend.birth)
            .memo(friend.memo)
            .useYn(useYn)
            .build();
    }
}
