package com.clucid.server.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {
	private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body,
		ClientHttpRequestExecution execution) throws IOException {
		log.info("=== Request ===");
		log.info("URI: {}", request.getURI());
		log.info("Method: {}", request.getMethod());
		log.info("Headers: {}", request.getHeaders());
		log.info("Body: {}", new String(body, StandardCharsets.UTF_8));

		ClientHttpResponse response = execution.execute(request, body);

		log.info("=== Response ===");
		log.info("Status code: {}", response.getStatusCode());
		byte[] responseBody = StreamUtils.copyToByteArray(response.getBody());

		// 로그 출력
		log.info("Response body: {}", new String(responseBody, StandardCharsets.UTF_8));

		// 복사한 바이트로 새 InputStream 제공
		return new BufferingClientHttpResponseWrapper(response, responseBody);
	}
}
