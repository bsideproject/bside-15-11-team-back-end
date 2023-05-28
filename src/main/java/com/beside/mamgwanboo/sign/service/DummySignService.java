package com.beside.mamgwanboo.sign.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.beside.mamgwanboo.user.document.User;
import com.beside.mamgwanboo.user.repository.UserRepository;
import protobuf.type.OauthServiceType;
import reactor.core.publisher.Mono;

@Service
public class DummySignService extends AbstractSignService {
	private final UserRepository userRepository;

	public DummySignService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	Mono<String> getAccessToken(
		OauthServiceType oauthServiceType, String authenticationCode
	) {
		return Mono.just("dummy");
	}

	@Override
	Mono<AbstractSignService.UserInformation> getUserInformation(
		OauthServiceType oauthServiceType, String accessToken
	) {
		return Mono.just(UserInformation.builder()
			.oauthServiceType(oauthServiceType)
			.serviceUserId(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
			.build());
	}

	@Override
	Mono<Boolean> isNewUser(User user) {
		return Mono.just(true);
	}

	@Override
	Mono<String> signUp(User user) {
		return userRepository.save(user)
			.map(User::getSequence);
	}
}
