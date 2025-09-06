package com.clucid.server.terms.entity.model.external;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateTermResponse {

	private Result result;

	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Getter
	@Setter
	public static class Result {

		private String term;

		private List<String> tags;

		@JsonProperty("definition_by_tags")
		private Map<String, String> definitionByTags;

		@JsonProperty("examples_by_tag")
		private Map<String, List<String>> examplesByTag;

		@JsonProperty("ko_term")
		private String koTerm;

		@JsonProperty("en_term")
		private String enTerm;
	}
}
