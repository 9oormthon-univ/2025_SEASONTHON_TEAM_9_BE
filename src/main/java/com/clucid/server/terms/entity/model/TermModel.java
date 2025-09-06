package com.clucid.server.terms.entity.model;

import java.util.List;

import com.clucid.server.terms.persist.jpa.TermJpaEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TermModel {
	private String id;
	private String nameKr;
	private String nameEn;
	// private String definition;
	private List<String> definitions;
	private List<String> examples;

	private String imgUrl;
	public static TermModel fromJpa(TermJpaEntity entity){
		return TermModel.builder()
			.id(entity.getId())
			.nameKr(entity.getTermKorean())
			.nameEn(entity.getTermEnglish())
			.definitions(entity.getDefinitions())
			.imgUrl(entity.getImgUrl())
			.build();
	}
	public static TermJpaEntity toJpa(TermModel model){
		return TermJpaEntity.builder()
			.termKorean(model.getNameKr())
			.termEnglish(model.getNameEn())
			.definitions(model.getDefinitions())
			.examples(model.getExamples())
			.imgUrl(model.getImgUrl())
			.build();
	}
}
