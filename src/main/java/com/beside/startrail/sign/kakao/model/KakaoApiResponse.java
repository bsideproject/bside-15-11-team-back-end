package com.beside.startrail.sign.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoApiResponse {
  private long id;
  @JsonProperty("kakao_account")
  private KakaoAccount kakaoAccount;
}
