package com.beside.startrail.user.model;

import com.beside.startrail.user.type.AgeRangeType;
import com.beside.startrail.user.type.SexType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInformation {
  private final String profileNickname;
  private final String profileImageLink;
  private final SexType sexType;
  private final AgeRangeType ageRangeType;
  private final LocalDateTime birth;
}
