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
import protobuf.common.BirthProto;
import protobuf.common.LevelInformationProto;

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
    private BirthProto birth;
    private String memo;

    @Builder.Default
    private LevelInformationProto levelInformation = LevelInformationProto.newBuilder().build();
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
