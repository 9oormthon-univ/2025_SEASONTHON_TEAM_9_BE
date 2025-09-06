package com.clucid.server.terms.entity.model.external;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TermSearchLlmRequest {
	private String mode;
	private String query;
	private int maxK = 5;

}
