package com.clucid.server.bookmark.entity.controller;

import java.util.List;

import com.clucid.server.terms.entity.model.TermOnlyNameAndDefModel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetBookmarksResponse {
	private List<TermOnlyNameAndDefModel> terms;
}
