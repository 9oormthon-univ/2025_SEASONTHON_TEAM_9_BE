package com.clucid.server.terms.entity.model;

import com.clucid.server.terms.persist.jpa.TagJpaEntity;
import com.clucid.server.terms.persist.jpa.TermTagJpaEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TagOnlyNameModel {
	private String id;
	private String name;

	public static TagOnlyNameModel fromTermTagJpa(TermTagJpaEntity jpa) {
		return TagOnlyNameModel.builder()
				.id(jpa.getId())
				.name(jpa.getTagName())
				.build();
	}

}
