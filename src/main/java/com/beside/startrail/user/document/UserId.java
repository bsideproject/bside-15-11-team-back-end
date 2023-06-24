package com.beside.startrail.user.document;

import com.beside.startrail.user.type.OauthServiceType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class UserId {
  private final OauthServiceType oauthServiceType;
  private final String serviceUserId;
}
