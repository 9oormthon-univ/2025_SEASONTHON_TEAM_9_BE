package com.clucid.server.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.clucid.server.auth.jwt.JwtFilter;
import com.clucid.server.auth.jwt.JwtUtil;
import com.clucid.server.auth.action.login.CustomLoginFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final AuthenticationConfiguration authenticationConfiguration;
	private final JwtUtil jwtUtil;
	// private final UserAuthService userAuthService;

	// private final CustomOAuth2UserService customOAuth2UserService;

	// private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.csrf(AbstractHttpConfigurer::disable);

		http
			.formLogin((auth) -> auth.disable());

		http
			.httpBasic((auth) -> auth.disable());
		http.csrf((csrf) -> csrf.disable());
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

		// TODO : 이 부분은 나중에 다시 확인해야함
		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/", "/api/login/reissue","/api/signup/**"
					, "/api/**", "/error/**"
					,"/v3/api-docs/**", "/swagger-ui/**"
					, "/oauth2/authorization/**").permitAll()
			);//일단 /api/** 는 임시로..

		http
			.sessionManagement((session) ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));



		http
			.addFilterBefore(new JwtFilter(jwtUtil), CustomLoginFilter.class);
		http
			.addFilterAt(new CustomLoginFilter
					(authenticationManager(authenticationConfiguration), jwtUtil)
				, UsernamePasswordAuthenticationFilter.class);

		// TODO: 로그아웃 필터를 추가하기
/*		http
			.addFilterBefore(new CustomLogoutFilter(userAuthService)
				, LogoutFilter.class);*/

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		// 운영 + 로컬 모두 허용
		config.setAllowedOriginPatterns(List.of(
			"https://*.clucid.online",
			"http://localhost:*"
		));

		// HTTP 메서드 허용
		config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS","HEAD"));

		// 모든 헤더 허용
		config.setAllowedHeaders(List.of("*"));

		// 필요한 응답 헤더 노출 (파일 다운로드 등)
		config.setExposedHeaders(List.of("Authorization", "Content-Disposition"));

		// 인증 정보(쿠키/Authorization 헤더) 포함 허용
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	/*
		http
			.oauth2Login((oauth2) ->
				oauth2
					.loginPage("/login")
					.loginProcessingUrl("/api/login/oauth2/code/*")
					.userInfoEndpoint(userInfoEndpointConfig ->
						userInfoEndpointConfig.userService(customOAuth2UserService))
					.successHandler(customOAuth2SuccessHandler));
*/


}
