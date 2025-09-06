package com.clucid.server.auth.persist;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity,String> {
	Optional<UserJpaEntity> findByEmail(String email);
}
