package com.beside.mamgwanboo.sign.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoOauthResponse {
  @JsonProperty("access_token")
  private String accessToken;
}
