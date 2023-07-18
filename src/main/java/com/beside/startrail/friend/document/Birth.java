package com.beside.startrail.friend.document;

import com.beside.startrail.common.type.YnType;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Birth {
  private YnType isLunar;
  private LocalDate date;
}
