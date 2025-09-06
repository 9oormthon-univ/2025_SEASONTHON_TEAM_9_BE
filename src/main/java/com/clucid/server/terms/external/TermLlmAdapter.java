package com.clucid.server.terms.external;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.clucid.server.terms.entity.model.external.CreateTermResponse;
import com.clucid.server.terms.entity.model.external.CreateTermResult;
import com.clucid.server.terms.entity.model.external.TermCreateLlmRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TermLlmAdapter {
	private final RestTemplate restTemplate;
	private final Map<String,String> tagWithOnLlmConvertMap;

	private final String DJANGO_URL = "http://49.50.134.173:8000/";
	// private final String DJANGO_URL = "http://localhost:8000/";

	public CreateTermResult createTerm(String term) {

		TermCreateLlmRequest request = TermCreateLlmRequest.builder()
			.term(term)
			.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<TermCreateLlmRequest> entity = new HttpEntity<>(request, headers);

		CreateTermResponse response = restTemplate.postForObject(
			DJANGO_URL + "create/terms/",
			entity,
			CreateTermResponse.class
		);

		return CreateTermResult.fromResponse(response,tagWithOnLlmConvertMap);
	}

}
