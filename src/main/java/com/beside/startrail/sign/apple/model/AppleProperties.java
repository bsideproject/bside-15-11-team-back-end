package com.beside.startrail.sign.apple.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.beside.startrail.user.type.OauthServiceType;
import jakarta.annotation.PostConstruct;

@ConfigurationProperties("oauth.apple")
public class AppleProperties {
	private final String accessTokenUri;
	private final String apiUri;
	private final String redirectUri;
	private final String clientId;

	public AppleProperties(
		String accessTokenUri,
		String apiUri,
		String redirectUri,
		String clientId
	) {
		this.accessTokenUri = accessTokenUri;
		this.apiUri = apiUri;
		this.redirectUri = redirectUri;
		this.clientId = clientId;
	}

	@PostConstruct
	void inject() {
		OauthServiceType.APPLE.getInformation().put(OauthServiceType.ACCESS_TOKEN_URI, accessTokenUri);
		OauthServiceType.APPLE.getInformation().put(OauthServiceType.API_URI, apiUri);
		OauthServiceType.APPLE.getInformation().put(OauthServiceType.REDIRECT_URI, redirectUri);
		OauthServiceType.APPLE.getInformation().put(OauthServiceType.CLIENT_ID, clientId);
	}
}
