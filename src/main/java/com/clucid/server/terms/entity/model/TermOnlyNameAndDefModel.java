package com.clucid.server.terms.entity.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TermOnlyNameAndDefModel {
	private String id;
	private String nameKr;
	private List<String> definitions;
	private List<TagOnlyNameModel> tags;
}
