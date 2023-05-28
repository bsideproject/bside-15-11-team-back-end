package com.beside.mamgwanboo.sign.service;

import java.time.LocalDateTime;

import com.beside.mamgwanboo.user.document.User;
import protobuf.type.OauthServiceType;
import protobuf.type.SexType;
import reactor.core.publisher.Mono;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public abstract class AbstractSignService {

	public Mono<String> sign(
		OauthServiceType oauthServiceType,
		String authenticationCode
	) {
		return getAccessToken(oauthServiceType, authenticationCode)
			.flatMap(accessToken -> getUserInformation(oauthServiceType, accessToken))
			.flatMap(this::makeUser)
			.flatMap(user -> isNewUser(user)
				.filter(Boolean::booleanValue)
				.flatMap(__ -> signUp(user))
				.switchIfEmpty(Mono.fromSupplier(user::getSequence))
			);
	}

	private Mono<User> makeUser(UserInformation userInformation) {
		return Mono.just(User.builder()
			.oauthServiceType(userInformation.oauthServiceType)
			.serviceUserId(userInformation.serviceUserId)
			.profileNickname(userInformation.profileNickname)
			.profileImageLink(userInformation.profileImageLink)
			.sexType(userInformation.sexType)
			.age(userInformation.age)
			.birthDate(userInformation.birthDate)
			.build());
	}

	abstract Mono<String> getAccessToken(
		OauthServiceType oauthServiceType,
		String authenticationCode
	);

	abstract Mono<UserInformation> getUserInformation(
		OauthServiceType oauthServiceType,
		String accessToken
	);

	abstract Mono<Boolean> isNewUser(User user);

	abstract Mono<String> signUp(User user);

	@Builder
	@AllArgsConstructor
	@Getter
	protected static class UserInformation {
		private OauthServiceType oauthServiceType;
		private String serviceUserId;
		private String profileNickname;
		private String profileImageLink;
		private SexType sexType;
		private Integer age;
		private LocalDateTime birthDate;
	}
}
