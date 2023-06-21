package com.beside.startrail.user.document;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import protobuf.common.type.OauthServiceType;

@Builder
@Getter
@EqualsAndHashCode
public class UserId {
  private final OauthServiceType oauthServiceType;
  private final String serviceUserId;
}
