package com.beside.mamgwanboo.sign.kakao.service;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.sign.common.service.AbstractSignService;
import com.beside.mamgwanboo.sign.kakao.model.KakaoApiResponse;
import com.beside.mamgwanboo.sign.kakao.model.KakaoOauthRequest;
import com.beside.mamgwanboo.sign.kakao.model.KakaoOauthResponse;
import com.beside.mamgwanboo.user.document.User;
import com.beside.mamgwanboo.user.model.Birth;
import com.beside.mamgwanboo.user.model.UserInformation;
import com.beside.mamgwanboo.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import protobuf.common.type.OauthServiceType;
import reactor.core.publisher.Mono;

@Service
public class KakaoSignService extends AbstractSignService {
  private final String oauthUri;
  private final String apiUri;
  private final String clientId;
  private final String redirectUri;
  private final String clientSecret;
  private final UserRepository userRepository;
  private final WebClient webClient;

  public KakaoSignService(@Value("${oauth.kakao.uri}") String oauthUri,
                          @Value("${api.kakao.uri}") String apiUri,
                          @Value("${oauth.kakao.clientId}") String clientId,
                          @Value("${oauth.kakao.redirectUri}") String redirectUri,
                          @Value("${oauth.kakao.clientSecret}") String clientSecret,
                          UserRepository userRepository) {
    this.oauthUri = oauthUri;
    this.apiUri = apiUri;
    this.clientId = clientId;
    this.redirectUri = redirectUri;
    this.clientSecret = clientSecret;
    this.userRepository = userRepository;
    this.webClient = WebClient.builder()
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .build();
  }

  @Override
  public boolean isTargetService(OauthServiceType oauthServiceType) {
    return OauthServiceType.KAKAO.equals(oauthServiceType);
  }

  @Override
  public Mono<String> getAccessToken(String code) {
    return webClient.post().uri(oauthUri)
        .body(BodyInserters.fromFormData(makeOauthRequest(code).toFormData())).retrieve()
        .bodyToMono(KakaoOauthResponse.class).map(KakaoOauthResponse::getAccessToken);
  }

  @Override
  public Mono<UserInformation> getUserInformation(OauthServiceType oauthServiceType,
                                                  String accessToken) {
    return webClient.get().uri(apiUri)
        .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken)).retrieve()
        .bodyToMono(KakaoApiResponse.class)
        .map(kakaoApiResponse -> toUserInformation(oauthServiceType, kakaoApiResponse));
  }

  @Override
  public Mono<User> getUser(OauthServiceType oauthServiceType, String serviceUserId, YnType useYn) {
    return userRepository
        .findUserByUserInformation_OauthServiceTypeAndUserInformation_ServiceUserIdAndUseYn(
            oauthServiceType, serviceUserId, useYn);
  }

  @Override
  public Mono<User> signUp(User user) {
    return userRepository.save(user);
  }

  private KakaoOauthRequest makeOauthRequest(String authenticationCode) {
    return KakaoOauthRequest.builder().clientId(clientId).redirectUri(redirectUri)
        .clientSecret(clientSecret).code(authenticationCode).build();
  }

  private UserInformation toUserInformation(OauthServiceType oauthServiceType,
                                            KakaoApiResponse kakaoApiResponse) {
    return UserInformation.builder().oauthServiceType(oauthServiceType)
        .serviceUserId(String.valueOf(kakaoApiResponse.getId()))
        .profileNickname(kakaoApiResponse.getKakaoAccount().getKakaoProfile().getNickname())
        .profileImageLink(kakaoApiResponse.getKakaoAccount().getKakaoProfile().getProfileImageUrl())
        .sexType(kakaoApiResponse.getKakaoAccount().getKakaoGenderType().getSexType())
        .ageRangeType(kakaoApiResponse.getKakaoAccount().getKakaoAgeRangeType().getAgeRangeType())
        .birth(Birth.builder()
            .isLunar(kakaoApiResponse.getKakaoAccount().getKakaoBirthdayType().getIsLunar()).date(
                toLocalDate(kakaoApiResponse.getKakaoAccount().getBirthYear(),
                    kakaoApiResponse.getKakaoAccount().getBirthDay())).build()).build();
  }

  private LocalDate toLocalDate(Year year, MonthDay monthDay) {
    if (Objects.isNull(year) || Objects.isNull(monthDay)) {
      return null;
    }

    return monthDay.atYear(year.getValue());
  }
}
