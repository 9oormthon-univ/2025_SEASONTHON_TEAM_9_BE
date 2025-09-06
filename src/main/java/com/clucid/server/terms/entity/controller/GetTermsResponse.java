package com.clucid.server.terms.entity.controller;

import java.util.List;

import com.clucid.server.terms.entity.model.TermWithTagAndRelModel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetTermsResponse {
	private List<GetOneTermResponse> terms;
	public static GetTermsResponse fromModel(List<TermWithTagAndRelModel> termWithTagAndRelModels) {
		List<GetOneTermResponse> terms = termWithTagAndRelModels.stream()
			.map(GetOneTermResponse::fromModel)
			.toList();
		return new GetTermsResponse(terms);
	}
}
