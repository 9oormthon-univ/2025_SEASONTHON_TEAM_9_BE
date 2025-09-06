package com.clucid.server.auth.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.clucid.server.auth.entity.UserAuthEntity;
import com.clucid.server.auth.entity.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String authorization = request.getHeader("Authorization");

		if (authorization == null || !authorization.startsWith("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authorization.split(" ")[1];

		boolean isAccessValid = isAccessValid(token);
		if (!isAccessValid) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String sub = jwtUtil.getSub(token);
		String role = jwtUtil.getRole(token);

		UserAuthEntity userAuthEntity = UserAuthEntity.builder()
			.id(sub)
			.password("temp password")
			.role(role)
			.build();

		UserDetailsImpl userDetails = new UserDetailsImpl(userAuthEntity);
		Authentication authToken = new UsernamePasswordAuthenticationToken(
			userDetails, null, userDetails.getAuthorities()
		);

		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}

	private boolean isAccessValid(String token) {

		try {
			jwtUtil.isExpired(token);
		} catch (ExpiredJwtException e) {
			log.error("JWT expired: {}", e.getMessage());
			return false;
		}
		return true;
	}

}
