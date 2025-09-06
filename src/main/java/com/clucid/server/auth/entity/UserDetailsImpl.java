package com.clucid.server.auth.entity;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {
	private final UserAuthEntity userAuthEntity;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return userAuthEntity.getRole();
			}
		});
		return collection;
	}
	public String getId() {
		return userAuthEntity.getId();
	}

	@Override
	public String getUsername() {
		return userAuthEntity.getEmail();
	}
	public String getNickname() {
		return userAuthEntity.getNickname();
	}

	@Override
	public String getPassword() {
		return userAuthEntity.getPassword();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
