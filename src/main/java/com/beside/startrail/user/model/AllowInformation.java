package com.beside.startrail.user.model;

import com.beside.startrail.common.type.YnType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AllowInformation {
  @Builder.Default
  private final YnType serviceYn = YnType.N;
  @Builder.Default
  private final YnType privateInformationYn = YnType.N;
  @Builder.Default
  private final YnType eventMarketingYn = YnType.N;
}
