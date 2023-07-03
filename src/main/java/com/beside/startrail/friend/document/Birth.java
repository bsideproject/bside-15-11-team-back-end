package com.beside.startrail.friend.document;

import com.beside.startrail.common.type.YnType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class Birth {
    @Builder.Default
    private YnType isLunar = YnType.N;
    @Builder.Default
    private LocalDate date = LocalDate.now();
}
