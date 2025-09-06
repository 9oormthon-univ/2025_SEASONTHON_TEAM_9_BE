package com.clucid.server.terms.entity.model.external;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateTermResultUnit{

	private String term;

	@JsonProperty("ko_term")
	private String termKr;

	@JsonProperty("en_term")
	private String termEn;

	@JsonProperty("definition_by_tags")
	private Map<String, String> definitionByTags;

	@JsonProperty("examples_by_tags")
	private Map<String, String> examplesByTags;

	private List<String> tags;
}

