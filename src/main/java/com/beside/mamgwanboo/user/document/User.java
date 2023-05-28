package com.beside.mamgwanboo.user.document;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import protobuf.type.LevelInformation;
import protobuf.type.OauthServiceType;
import protobuf.type.SexType;
import protobuf.type.UseYnType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
@Document("user")
public class User {
	@Id
	private String sequence;
	private OauthServiceType oauthServiceType;
	private String serviceUserId;
	private String profileNickname;
	private String profileImageLink;
	private SexType sexType;
	private Integer age;
	private LocalDateTime birthDate;
	private LevelInformation levelInformation;
	private UseYnType useYnType;
}
