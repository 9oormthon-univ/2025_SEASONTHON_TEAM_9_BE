package com.clucid.server.terms.entity.controller;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class RealCreateTermRequest {
	private String term;

	private String termKr;

	private String termEn;

	private List<String> tags;

	private List<String> definitions;

	private List<String> examples;
	private String imgUrl;
}
