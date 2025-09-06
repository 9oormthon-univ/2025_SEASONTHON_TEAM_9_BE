package com.clucid.server.auth.persist;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.clucid.server.auth.application.UserRepository;
import com.clucid.server.auth.entity.UserAuthEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final UserJpaRepository userJpaRepository;
	@Override
	public Optional<UserAuthEntity> findByEmail(String email) {
		return userJpaRepository.findByEmail(email).map(UserAuthEntity::fromJpa);
	}

	@Override
	public void saveUser(String email, String password, String role, String nickname) {
		UserJpaEntity userJpaEntity =UserJpaEntity.builder()
				.email(email)
				.password(password)
				.role(role)
				.nickname(nickname)
				.build();
		userJpaRepository.save(userJpaEntity);
	}
}
