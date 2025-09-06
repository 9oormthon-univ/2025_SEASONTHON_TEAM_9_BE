package com.clucid.server.terms.entity.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OneTagResponse {
	private String id;
	private String name;
	private int refCount;
}
