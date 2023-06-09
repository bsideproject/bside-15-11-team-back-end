package com.beside.mamgwanboo.sign.common.model;

import com.beside.mamgwanboo.user.document.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignResult {
  private final User user;
  private final boolean isNewUser;
}
