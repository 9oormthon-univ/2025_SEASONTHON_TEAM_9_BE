package com.clucid.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {


		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(10_000); // 연결 타임아웃 10초
		factory.setReadTimeout(120_000);   // 읽기(응답) 타임아웃 120초
		RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.getInterceptors().add(new LoggingInterceptor());
		return restTemplate;
	}

}
