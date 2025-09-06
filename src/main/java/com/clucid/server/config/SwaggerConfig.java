package com.clucid.server.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@SecurityScheme(
	name = "JWT",
	type = SecuritySchemeType.HTTP,
	scheme = "Bearer",
	bearerFormat = "JWT",
	description = "JWT 인증을 위한 Bearer Token을 사용합니다. \n 프론트엔드 입력에선 Bearer 을 작성 해야하지만 이 입력에는 Bearer 텍스트를 적지 않아도 됩니다"
)
@Configuration
public class SwaggerConfig {

	@Value("${custom.openapi.server-url}")
	private String serverUrl;

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.servers(List.of(
				new Server().url(serverUrl)
				, new Server().url("http://localhost:8080").description("Local development server")
			));
	}
}
