package com.clucid.server.terms.entity.model.external;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateTermResult {


	private String term;

	private List<String> tags;

	private List<String> definitions;

	private List<String> examples;

	private String termKr;

	private String termEn;

	public static CreateTermResult fromResponse(CreateTermResponse response, Map<String, String> tagWithOnLlmConvertMap) {
		List<String> convertedTags = response.getResult().getTags().stream()
			.map(tag -> tagWithOnLlmConvertMap.getOrDefault(tag, tag))
			.toList();
		return CreateTermResult.builder()
			.term(response.getResult().getTerm())
			.tags(convertedTags)
			.definitions(response.getResult().getDefinitionByTags().values().stream().toList())
			.examples(response.getResult().getExamplesByTag().values().stream().flatMap(List::stream).toList())
			.termKr(response.getResult().getKoTerm())
			.termEn(response.getResult().getEnTerm())
			.build();
	}
}
