package com.beside.startrail.sign.kakao.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.sign.kakao.model.KakaoAccessTokenRequest;
import com.beside.startrail.sign.kakao.model.KakaoAccessTokenResponse;
import com.beside.startrail.sign.kakao.model.KakaoApiResponse;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.document.UserId;
import com.beside.startrail.user.model.UserInformation;
import com.beside.startrail.user.type.OauthServiceType;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class KakaoSignCommand {
  private static final WebClient WEB_CLIENT = WebClient.builder()
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
      .build();
  private final String accessTokenUri;
  private final String apiUri;
  private final String redirectUri;
  private final String clientId;
  private final String code;
  private Mono<User> result;

  public KakaoSignCommand(
      String accessTokenUri,
      String apiUri,
      String code,
      String redirectUri,
      String clientId
  ) {
    this.code = code;
    this.accessTokenUri = accessTokenUri;
    this.apiUri = apiUri;
    this.redirectUri = redirectUri;
    this.clientId = clientId;
  }

  public Mono<User> execute() {
    this.result = makeUser();

    return result;
  }

  private Mono<User> makeUser() {
    return Mono.just(
            KakaoAccessTokenRequest.builder()
                .clientId(clientId)
                .redirectUri(redirectUri)
                .code(code)
                .build()
        )
        .flatMap(kakaoAccessTokenRequest ->
            WEB_CLIENT
                .post()
                .uri(accessTokenUri)
                .body(BodyInserters.fromFormData(kakaoAccessTokenRequest.toMultiValueMap()))
                .retrieve()
                .bodyToMono(KakaoAccessTokenResponse.class)
                .map(KakaoAccessTokenResponse::getAccessToken)
        )
        .flatMap(accessToken ->
            WEB_CLIENT
                .get()
                .uri(apiUri)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken))
                .retrieve()
                .bodyToMono(KakaoApiResponse.class)
                .map(kakaoApiResponse ->
                    User.builder()
                        .userId(
                            UserId.builder()
                                .oauthServiceType(OauthServiceType.KAKAO)
                                .serviceUserId(String.valueOf(kakaoApiResponse.getId()))
                                .build()
                        )
                        .sequence(UUID.randomUUID().toString())
                        .userInformation(
                            UserInformation.builder()
                                .profileNickname(
                                    kakaoApiResponse.getKakaoAccount().getKakaoProfile()
                                        .getNickname())
                                .profileImageUrl(
                                    kakaoApiResponse.getKakaoAccount().getKakaoProfile()
                                        .getProfileImageUrl())
                                .sexType(kakaoApiResponse.getKakaoAccount().getKakaoGenderType()
                                    .getSexType())
                                .ageRangeType(
                                    kakaoApiResponse.getKakaoAccount().getKakaoAgeRangeType()
                                        .getAgeRangeType())
                                .birth(kakaoApiResponse.getKakaoAccount().getBirth())
                                .build()
                        )
                        .useYn(YnType.Y)
                        .allowPrivateInformationYn(YnType.N)
                        .build()
                )
        );
  }
}
