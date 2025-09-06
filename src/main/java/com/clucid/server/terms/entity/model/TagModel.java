package com.clucid.server.terms.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TagModel {
	private String id;
	private String name;
	private int refCount;
}
