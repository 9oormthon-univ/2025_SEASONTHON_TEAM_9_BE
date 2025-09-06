package com.clucid.server.auth.action.signup;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SignupController {
	private final SignupUseCase signupUseCase;
	@PostMapping("/api/signup")
	public ResponseEntity<Void> signupCustomer(@RequestBody SignupRequest request) {
		signupUseCase.signupUser(request.getEmail(), request.getPassword());
		return ResponseEntity.ok().build();
	}
}
