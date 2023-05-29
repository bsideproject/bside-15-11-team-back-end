package com.beside.mamgwanboo.sign.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.beside.mamgwanboo.sign.model.KakaoApiResponseDto;
import com.beside.mamgwanboo.sign.model.KakaoOauthRequestDto;
import com.beside.mamgwanboo.sign.model.KakaoOauthResponseDto;
import com.beside.mamgwanboo.user.document.User;
import com.beside.mamgwanboo.user.repository.UserRepository;
import io.netty.util.CharsetUtil;
import protobuf.type.OauthServiceType;
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

	public KakaoSignService(
		@Value("${oauth.kakao.baseUrl}") String baseUrl,
		@Value("${oauth.kakao.uri}") String oauthUri,
		@Value("${api.kakao.uri}") String apiUri,
		@Value("${oauth.kakao.clientId}") String clientId,
		@Value("${oauth.kakao.redirectUri}") String redirectUri,
		@Value("${oauth.kakao.clientSecret}") String clientSecret,
		UserRepository userRepository
	) {
		this.oauthUri = oauthUri;
		this.apiUri = apiUri;
		this.clientId = clientId;
		this.redirectUri = redirectUri;
		this.clientSecret = clientSecret;
		this.userRepository = userRepository;
		this.webClient = WebClient.builder()
			.baseUrl(baseUrl)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.defaultHeader("charset", CharsetUtil.UTF_8.name())
			.build();
	}

	@Override
	Mono<String> getAccessToken(String authenticationCode) {
		return webClient
			.post()
			.uri(oauthUri)
			.body(BodyInserters.fromPublisher(makeOauthTokenRequestDto(authenticationCode), KakaoOauthRequestDto.class))
			.retrieve()
			.bodyToMono(KakaoOauthResponseDto.class)
			.map(KakaoOauthResponseDto::getAccessToken);
	}

	@Override
	Mono<UserInformation> getUserInformation(OauthServiceType oauthServiceType, String accessToken) {
		return webClient
			.get()
			.uri(apiUri)
			.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken))
			.retrieve()
			.bodyToMono(KakaoApiResponseDto.class)
			.flatMap(kakaoApiResponseDto -> toUserInformation(oauthServiceType, kakaoApiResponseDto));
	}

	@Override
	Mono<Boolean> isNewUser(User user) {
		return userRepository.existsByOauthServiceTypeAndServiceUserId(user.getOauthServiceType(), user.getServiceUserId());
	}

	@Override
	Mono<String> signUp(User user) {
		return userRepository.save(user)
			.map(User::getSequence);
	}

	private Mono<KakaoOauthRequestDto> makeOauthTokenRequestDto(String authenticationCode) {
		return Mono.just(
			KakaoOauthRequestDto.builder()
				.clientId(clientId)
				.redirectUri(redirectUri)
				.code(authenticationCode)
				.clientSecret(clientSecret)
				.build()
		);
	}

	private Mono<UserInformation> toUserInformation(OauthServiceType oauthServiceType, KakaoApiResponseDto kakaoApiResponseDto) {
		return Mono.just(
			UserInformation.builder()
				.oauthServiceType(oauthServiceType)
				.serviceUserId(String.valueOf(kakaoApiResponseDto.getId()))
				.profileNickname(kakaoApiResponseDto.getKakaoProfile().getNickname())
				.profileImageLink(kakaoApiResponseDto.getKakaoProfile().getProfileImageUrl())
				// todo
				// .sexType()
				// .age()
				// .birthDate()
				.build()
		);
	}
}
