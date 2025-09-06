package com.clucid.server.auth.entity;

import com.clucid.server.auth.persist.UserJpaEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserAuthEntity {
	private String id;
	private String email;
	private String password;
	private String nickname;
	private String role;

	@Builder
	public UserAuthEntity(String id, String email, String password, String nickname, String role) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
	}

	public static UserAuthEntity fromJpa(UserJpaEntity userJpaEntity) {
		if (userJpaEntity == null) {
			return null;
		}
		return UserAuthEntity.builder()
			.id(userJpaEntity.getId())
			.email(userJpaEntity.getEmail())
			.password(userJpaEntity.getPassword())
			.nickname(userJpaEntity.getNickname())
			.role(userJpaEntity.getRole())
			.build();
	}
}
