package com.beside.startrail.sign.apple.command;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.reactive.function.client.WebClient;

import com.beside.startrail.sign.apple.model.AppleAccessTokenRequest;
import com.beside.startrail.sign.apple.model.AppleAccessTokenResponse;
import com.beside.startrail.sign.common.command.AbstractSignCommand;
import com.beside.startrail.sign.kakao.model.KakaoAccessTokenResponse;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.type.OauthServiceType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

public class AppleSignCommand extends AbstractSignCommand {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private final WebClient webClient;
	private final URI accessTokenUri;
	private final String apiUri;
	private final String redirectUri;
	private final String clientId;
	private final String clientSecret;

	public AppleSignCommand(
		WebClient webClient,
		String accessTokenUri,
		String apiUri,
		String code,
		String redirectUri,
		String clientId,
		String clientSecret
	) {
		super(OauthServiceType.APPLE, code);
		this.webClient = webClient;

		try {
			this.accessTokenUri = new URI(accessTokenUri);
		} catch (URISyntaxException uriSyntaxException) {
			throw new IllegalArgumentException(String.format("accessTokenUri가 잘못되었습니다. accessTokenUri: %s", accessTokenUri));
		}

		this.apiUri = apiUri;
		this.redirectUri = redirectUri;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	@Override
	protected Mono<User> makeUser() {
		return Mono.just(
				AppleAccessTokenRequest.builder()
					.clientId(clientId)
					.clientSecret(clientSecret)
					.redirectUri(redirectUri)
					.build()
			)
			.flatMap(appleAccessTokenRequest ->
				webClient
					.post()
					.uri(uriBuilder ->
						uriBuilder
							.scheme(accessTokenUri.getScheme())
							.host(accessTokenUri.getHost())
							.path(accessTokenUri.getPath())
							.queryParams(appleAccessTokenRequest.toMultiValueMap())
							.build()
					)
					.retrieve()
					.bodyToMono(AppleAccessTokenResponse.class)
					.map(AppleAccessTokenResponse::getData)
					.map(AppleAccessTokenResponse.Data::getIdToken)
			)
			.map(idToken ->
				Jwts.parserBuilder()
					.build()
					.parseClaimsJws(idToken)
					.getBody()
			)
			.<String>handle((claims, sink) -> {
				try {
					sink.next(OBJECT_MAPPER.writeValueAsString(claims));
				} catch (JsonProcessingException jsonProcessingException) {
					sink.error(new IllegalArgumentException(
						String.format("jwt payload의 json 변환에 실패했습니다. jwt: %s", claims),
						jsonProcessingException
					));
				}
			})
			// todo
			.map(accessToken ->
				User.builder().build()
			);
	}
}
