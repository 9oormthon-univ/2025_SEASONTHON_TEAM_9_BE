package com.clucid.server.terms.entity.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TermIdAndRelsModel {
	private String id;
	private List<TermOnlyNameAndDefModel> relatedTerms;
}
