package com.beside.startrail.sign.apple.model;

import com.beside.startrail.user.type.OauthServiceType;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("oauth.apple")
public class AppleProperties {
  private final String accessTokenUri;
  private final String redirectUri;
  private final String key;
  private final String teamId;
  private final String clientId;
  private final String audience;
  private final String keyPath;

  public AppleProperties(
      String accessTokenUri,
      String redirectUri,
      String key,
      String teamId,
      String clientId,
      String audience,
      String keyPath
  ) {
    this.accessTokenUri = accessTokenUri;
    this.redirectUri = redirectUri;
    this.key = key;
    this.teamId = teamId;
    this.clientId = clientId;
    this.audience = audience;
    this.keyPath = keyPath;
  }

  @PostConstruct
  void inject() {
    OauthServiceType.APPLE.getInformation().put(OauthServiceType.ACCESS_TOKEN_URI, accessTokenUri);
    OauthServiceType.APPLE.getInformation().put(OauthServiceType.REDIRECT_URI, redirectUri);
    OauthServiceType.APPLE.getInformation().put(OauthServiceType.KEY, key);
    OauthServiceType.APPLE.getInformation().put(OauthServiceType.TEAM_ID, teamId);
    OauthServiceType.APPLE.getInformation().put(OauthServiceType.CLIENT_ID, clientId);
    OauthServiceType.APPLE.getInformation().put(OauthServiceType.AUDIENCE, audience);
    OauthServiceType.APPLE.getInformation().put(OauthServiceType.KEY_PATH, keyPath);
  }
}
