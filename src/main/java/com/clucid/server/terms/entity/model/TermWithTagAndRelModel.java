package com.clucid.server.terms.entity.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
public class TermWithTagAndRelModel {
	private String id;
	private String nameKr;
	private String nameEn;
	private List<String> definitions;
	private List<String> examples;
	private String imgUrl;
	private List<TagOnlyNameModel> tags;
	private List<TermOnlyNameAndDefModel> relations;
	@Setter
	@Builder.Default
	private Boolean isBookmarked = false;
}
