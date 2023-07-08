package com.beside.startrail.sign.kakao.command;

import com.beside.startrail.sign.common.command.AbstractSignCommand;
import com.beside.startrail.sign.kakao.model.KakaoAccessTokenRequest;
import com.beside.startrail.sign.kakao.model.KakaoAccessTokenResponse;
import com.beside.startrail.sign.kakao.model.KakaoApiResponse;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.document.UserId;
import com.beside.startrail.user.model.UserInformation;
import com.beside.startrail.user.type.OauthServiceType;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class KakaoSignCommand extends AbstractSignCommand {
  private final WebClient webClient;
  private final String accessTokenUri;
  private final String apiUri;
  private final String redirectUri;
  private final String clientId;

  public KakaoSignCommand(
      WebClient webClient,
      String accessTokenUri,
      String apiUri,
      String code,
      String redirectUri,
      String clientId
  ) {
    super(OauthServiceType.KAKAO, code);
    this.webClient = webClient;
    this.accessTokenUri = accessTokenUri;
    this.apiUri = apiUri;
    this.redirectUri = redirectUri;
    this.clientId = clientId;
  }

  @Override
  protected Mono<User> makeUser() {
    return Mono.just(
            KakaoAccessTokenRequest.builder()
                .clientId(clientId)
                .redirectUri(redirectUri)
                .code(super.code)
                .build()
        )
        .flatMap(kakaoAccessTokenRequest ->
            webClient
                .post()
                .uri(accessTokenUri)
                .body(BodyInserters.fromFormData(kakaoAccessTokenRequest.toMultiValueMap()))
                .retrieve()
                .bodyToMono(KakaoAccessTokenResponse.class)
                .map(KakaoAccessTokenResponse::getAccessToken)
        )
        .flatMap(accessToken ->
            webClient
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
                        .build()
                )
        );
  }
}
