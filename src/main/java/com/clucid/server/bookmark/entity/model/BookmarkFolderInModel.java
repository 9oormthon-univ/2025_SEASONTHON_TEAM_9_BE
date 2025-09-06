package com.clucid.server.bookmark.entity.model;

import java.time.LocalDateTime;

import com.clucid.server.bookmark.persist.jpa.BookmarkFolderJpaEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class BookmarkFolderInModel {
	private String id;
	private String name;
	private Boolean isIn;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static BookmarkFolderInModel fromJpaWithInfo(BookmarkFolderJpaEntity jpa, Boolean isIn){
		return BookmarkFolderInModel.builder()
			.id(jpa.getId())
			.name(jpa.getName())
			.isIn(isIn)
			.createdAt(jpa.getCreatedAt())
			.updatedAt(jpa.getUpdatedAt())
			.build();
	}
}
