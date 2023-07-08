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
              && KAKAO.information.containsKey(OauthServiceType.API_URI)
              && KAKAO.information.containsKey(OauthServiceType.REDIRECT_URI)
              && KAKAO.information.containsKey(OauthServiceType.CLIENT_ID))
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
      // todo
      return new AppleSignCommand(code);
    }
  };

  public static final String ACCESS_TOKEN_URI = "ACCESS_TOKEN_URI";
  public static final String API_URI = "API_URI";
  public static final String REDIRECT_URI = "REDIRECT_URI";
  public static final String CLIENT_ID = "CLIENT_ID";

  private static final WebClient webClient = WebClient.builder()
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
      .build();

  private Map<String, String> information = new HashMap<>();

  public abstract AbstractSignCommand getSignCommand(String code);
}
