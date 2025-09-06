package com.clucid.server.terms.entity.usecase;

import java.util.List;

import com.clucid.server.terms.entity.controller.CreateTermRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateTermCommand {
	private String nameKr;
	private String nameEn;
	private String definition;
	private List<String> tags;
	private String imgUrl;

	public static CreateTermCommand fromRequest(CreateTermRequest request) {
		return CreateTermCommand.builder()
			.nameKr(request.getNameKr())
			.nameEn(request.getNameEn())
			.definition(request.getDefinition())
			.tags(request.getTags())
			.imgUrl(request.getImgUrl())
			.build();
	}
}
