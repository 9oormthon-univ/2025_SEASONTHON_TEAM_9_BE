package com.clucid.server.terms.controller;

import java.util.List;

import com.clucid.server.terms.entity.model.TagModel;

public interface TagUseCase {
	String createTag(String tagName);

	List<TagModel> getTagsByQuery(String query);
}
