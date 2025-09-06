package com.clucid.server.config;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.HttpHeaders;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

	private final ClientHttpResponse response;
	private final byte[] body;

	public BufferingClientHttpResponseWrapper(ClientHttpResponse response, byte[] body) {
		this.response = response;
		this.body = body;
	}

	@Override
	public InputStream getBody() {
		return new ByteArrayInputStream(body); // 복사된 바이트로 새 InputStream 제공
	}

	@Override
	public HttpStatusCode getStatusCode() throws IOException {
		return HttpStatusCode.valueOf(response.getRawStatusCode());
	}

	@Override
	public int getRawStatusCode() throws IOException {
		return response.getRawStatusCode();
	}

	@Override
	public String getStatusText() throws IOException {
		return response.getStatusText();
	}

	@Override
	public HttpHeaders getHeaders() {
		return response.getHeaders();
	}

	@Override
	public void close() {
		response.close();
	}
}
