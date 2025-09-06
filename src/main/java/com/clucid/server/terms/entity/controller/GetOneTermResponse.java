package com.clucid.server.terms.entity.controller;

import java.util.List;

import com.clucid.server.terms.entity.model.TagOnlyNameModel;
import com.clucid.server.terms.entity.model.TermModel;
import com.clucid.server.terms.entity.model.TermOnlyNameAndDefModel;
import com.clucid.server.terms.entity.model.TermWithTagAndRelModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
public class GetOneTermResponse {
	private String id;
	private String nameKr;
	private String nameEn;
	private String definition;
	private String imgUrl;
	private List<TagOnlyNameModel> tags;
	private List<TermOnlyNameAndDefModel> relations;
	public static GetOneTermResponse fromModel(TermWithTagAndRelModel termWithTagAndRelModel) {
		return GetOneTermResponse.builder()
			.id(termWithTagAndRelModel.getId())
			.nameKr(termWithTagAndRelModel.getNameKr())
			.nameEn(termWithTagAndRelModel.getNameEn())
			.definition(termWithTagAndRelModel.getDefinition())
			.imgUrl(termWithTagAndRelModel.getImgUrl())
			.tags(termWithTagAndRelModel.getTags())
			.relations(termWithTagAndRelModel.getRelations())
			.isBookmarked(termWithTagAndRelModel.getIsBookmarked())
			.build();
	}

	@Setter
	@Builder.Default
	private Boolean isBookmarked = false;
}
