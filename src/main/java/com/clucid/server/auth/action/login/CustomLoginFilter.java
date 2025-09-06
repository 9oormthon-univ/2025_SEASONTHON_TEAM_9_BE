package com.clucid.server.auth.action.login;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.clucid.server.auth.entity.UserDetailsImpl;
import com.clucid.server.auth.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	private final static String LOGIN_URL = "/api/login";

	public CustomLoginFilter(AuthenticationManager authenticationManager
		, JwtUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		setFilterProcessesUrl(LOGIN_URL);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		throws AuthenticationException {

		UsernamePasswordAuthenticationToken authToken;
		try {
			LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

			authToken = new UsernamePasswordAuthenticationToken
				(loginRequest.getEmail(), loginRequest.getPassword(), null);
		} catch (IOException e) {
			log.error("Login request parsing failed: {}", e.getMessage());
			authToken = new UsernamePasswordAuthenticationToken("", "", null);
		}
		return authenticationManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authentication) throws IOException, ServletException {

		UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
		String sub = userDetails.getId();
		String email = userDetails.getUsername();
		String nickname = userDetails.getNickname();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();

		String accessJwt = jwtUtil.createAccessJwt(sub, email,nickname,"ROLE_USER");
		response.addHeader("Authorization", "Bearer " + accessJwt);
		response.addHeader("Access-Control-Expose-Headers", "Authorization");

		// Cookie refreshCookie = userService.createRefreshJwt(uid, role);
		// response.addCookie(refreshCookie);

		response.setStatus(HttpStatus.OK.value());
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException {

		response.setStatus(401);
	}
}