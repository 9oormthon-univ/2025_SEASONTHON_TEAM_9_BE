package com.clucid.server.terms.entity.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class RelateTermsRequest {
	private String sourceTermId;
	private String targetTermId;
}
