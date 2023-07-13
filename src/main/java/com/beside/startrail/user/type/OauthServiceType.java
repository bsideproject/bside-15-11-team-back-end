package com.beside.startrail.user.type;

import com.beside.startrail.sign.apple.command.AppleSignCommand;
import com.beside.startrail.sign.common.command.AbstractSignCommand;
import com.beside.startrail.sign.kakao.command.KakaoSignCommand;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
public enum OauthServiceType {
  KAKAO {
    @Override
    public AbstractSignCommand getSignCommand(String code) {
      if (
          !(KAKAO.information.containsKey(OauthServiceType.ACCESS_TOKEN_URI)
              && APPLE.information.containsKey(OauthServiceType.API_URI)
              && KAKAO.information.containsKey(OauthServiceType.REDIRECT_URI)
              && KAKAO.information.containsKey(OauthServiceType.CLIENT_ID)
              && KAKAO.information.containsKey(OauthServiceType.KEY))
      ) {
        throw new IllegalStateException("OauthServiceType이 초기화되지 않았습니다.");
      }

      return new KakaoSignCommand(
          webClient,
          KAKAO.information.get(OauthServiceType.ACCESS_TOKEN_URI),
          KAKAO.information.get(OauthServiceType.API_URI),
          code,
          KAKAO.information.get(OauthServiceType.REDIRECT_URI),
          KAKAO.information.get(OauthServiceType.CLIENT_ID)
      );
    }
  },
  APPLE {
    @Override
    public AbstractSignCommand getSignCommand(String code) {
      if (
          !(APPLE.information.containsKey(OauthServiceType.ACCESS_TOKEN_URI)
              && APPLE.information.containsKey(OauthServiceType.REDIRECT_URI)
              && APPLE.information.containsKey(OauthServiceType.KEY)
              && APPLE.information.containsKey(OauthServiceType.TEAM_ID)
              && APPLE.information.containsKey(OauthServiceType.CLIENT_ID)
              && APPLE.information.containsKey(OauthServiceType.AUDIENCE)
              && APPLE.information.containsKey(OauthServiceType.KEY_PATH))
      ) {
        throw new IllegalStateException("OauthServiceType이 초기화되지 않았습니다.");
      }

      return new AppleSignCommand(
          webClient,
          APPLE.information.get(OauthServiceType.ACCESS_TOKEN_URI),
          code,
          APPLE.information.get(OauthServiceType.REDIRECT_URI),
          APPLE.information.get(OauthServiceType.KEY),
          APPLE.information.get(OauthServiceType.TEAM_ID),
          APPLE.information.get(OauthServiceType.CLIENT_ID),
          APPLE.information.get(OauthServiceType.AUDIENCE),
          APPLE.information.get(OauthServiceType.KEY_PATH)
          );
    }
  };

  public static final String ACCESS_TOKEN_URI = "ACCESS_TOKEN_URI";
  public static final String API_URI = "API_URI";
  public static final String REDIRECT_URI = "REDIRECT_URI";
  public static final String CLIENT_ID = "CLIENT_ID";
  public static final String KEY = "KEY";
  public static final String TEAM_ID = "TEAM_ID";
  public static final String AUDIENCE = "AUDIENCE";
  public static final String KEY_PATH = "KEY_PATH";

  private static final WebClient webClient = WebClient.builder()
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
      .build();

  private Map<String, String> information = new HashMap<>();

  public abstract AbstractSignCommand getSignCommand(String code);
}
