package com.beside.mamgwanboo.user.model;

import com.beside.mamgwanboo.common.type.YnType;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Birth {
  private YnType isLunar;
  private LocalDate date;
}
