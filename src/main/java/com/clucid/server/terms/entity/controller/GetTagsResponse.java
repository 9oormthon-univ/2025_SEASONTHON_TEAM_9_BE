package com.clucid.server.terms.entity.controller;

import java.util.List;

import com.clucid.server.terms.entity.model.TagModel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetTagsResponse {
	private List<OneTagResponse> tags;
	public static GetTagsResponse fromModelList(List<TagModel> tagsByQuery) {
		List<OneTagResponse> tagResponses = tagsByQuery.stream()
			.map(tagModel -> OneTagResponse.builder()
				.id(tagModel.getId())
				.name(tagModel.getName())
				.refCount(tagModel.getRefCount())
				.build())
			.toList();
		return new GetTagsResponse(tagResponses);
	}
}
