package com.clucid.server.terms.entity.controller;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateTermRequest {
	private String nameKr;
	private String nameEn;
	private String definition;
	private List<String> tags;
	private String imgUrl;
}
