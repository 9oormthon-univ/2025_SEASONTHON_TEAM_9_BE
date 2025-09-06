package com.clucid.server.auth.application;

import java.util.Optional;

import com.clucid.server.auth.entity.UserAuthEntity;

public interface UserRepository {
	Optional<UserAuthEntity> findByEmail(String email);

	void saveUser(String email, String password, String role, String nickname);
}
