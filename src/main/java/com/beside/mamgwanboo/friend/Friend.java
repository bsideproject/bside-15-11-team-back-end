package com.beside.mamgwanboo.friend;

import com.beside.mamgwanboo.common.type.YnType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import protobuf.common.LevelInformation;
import protobuf.friend.Birth;

import java.time.LocalDateTime;

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
    private LevelInformation levelInformation = LevelInformation.newBuilder().build();
    @Builder.Default
    private YnType useYn = YnType.Y;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    public Friend notUseYn(Friend friend){
        friend.useYn = YnType.N;
        return friend;
    }
}
