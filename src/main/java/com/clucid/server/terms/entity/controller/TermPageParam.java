package com.clucid.server.terms.entity.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class TermPageParam {
	private int page = 0;             // 기본값 0
	private int size = 30;            // 기본값 10
}
