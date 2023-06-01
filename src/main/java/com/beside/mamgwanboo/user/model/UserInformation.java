package com.beside.mamgwanboo.user.model;

import com.beside.mamgwanboo.user.type.AgeRangeType;
import com.beside.mamgwanboo.user.type.SexType;
import protobuf.common.type.OauthServiceType;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInformation {
	private OauthServiceType oauthServiceType;
	private String serviceUserId;
	private String profileNickname;
	private String profileImageLink;
	private SexType sexType;
	private AgeRangeType ageRangeType;
	private Birth birth;
}
