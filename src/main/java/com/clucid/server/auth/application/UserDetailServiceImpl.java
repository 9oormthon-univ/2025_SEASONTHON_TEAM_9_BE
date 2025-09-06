package com.clucid.server.auth.application;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.clucid.server.auth.entity.UserAuthEntity;
import com.clucid.server.auth.entity.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserAuthEntity userEntity = userRepository.findByEmail(email).orElseGet(null);
		return new UserDetailsImpl(userEntity);
	}
}
