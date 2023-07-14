package com.beside.startrail.sign.common.configuration;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.beside.startrail.sign.apple.handler.AppleSignHandler;
import com.beside.startrail.sign.kakao.handler.KakaoSignHandler;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

@Configuration
public class SignRouterConfiguration {
  private final String kakaoSignPath;
  private final String appleSignPath;

  public SignRouterConfiguration(
      @Value("${oauth.kakao.redirectUri}") String kakaoRedirectUri,
      @Value("${oauth.apple.redirectUri}") String appleRedirectUri
  ) throws URISyntaxException {
    this.kakaoSignPath = new URI(kakaoRedirectUri).getPath();
    this.appleSignPath = new URI(appleRedirectUri).getPath();
  }

  @Bean
  public RouterFunction<?> routeSign(
      KakaoSignHandler kakaoSignHandler,
      AppleSignHandler appleSignHandler
  ) {
    return route()
        .GET(kakaoSignPath, kakaoSignHandler)
        .POST(appleSignPath, appleSignHandler)
        .build();
  }
}

