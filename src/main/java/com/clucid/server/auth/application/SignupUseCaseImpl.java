package com.clucid.server.auth.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clucid.server.auth.action.signup.SignupUseCase;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupUseCaseImpl implements SignupUseCase{
	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final String ROLE_USER = "USER";

	@Override
	public void signupUser(String email, String password) {
		if(userRepository.findByEmail(email).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}
		userRepository.saveUser(email, passwordEncoder.encode(password)
			, ROLE_USER,makeNickname(email));
	}
	private String makeNickname(String email) {
		return email.split("@")[0];
	}
}
