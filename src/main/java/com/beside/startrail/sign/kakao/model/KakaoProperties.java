package com.beside.startrail.sign.kakao.model;

import com.beside.startrail.user.type.OauthServiceType;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("oauth.kakao")
public class KakaoProperties {
  private final String accessTokenUri;
  private final String apiUri;
  private final String redirectUri;
  private final String clientId;

  public KakaoProperties(
      String accessTokenUri,
      String apiUri,
      String redirectUri,
      String clientId
  ) {
    this.accessTokenUri = accessTokenUri;
    this.apiUri = apiUri;
    this.redirectUri = redirectUri;
    this.clientId = clientId;
  }

  @PostConstruct
  void inject() {
    OauthServiceType.KAKAO.getInformation().put(OauthServiceType.ACCESS_TOKEN_URI, accessTokenUri);
    OauthServiceType.KAKAO.getInformation().put(OauthServiceType.API_URI, apiUri);
    OauthServiceType.KAKAO.getInformation().put(OauthServiceType.REDIRECT_URI, redirectUri);
    OauthServiceType.KAKAO.getInformation().put(OauthServiceType.CLIENT_ID, clientId);
  }
}
