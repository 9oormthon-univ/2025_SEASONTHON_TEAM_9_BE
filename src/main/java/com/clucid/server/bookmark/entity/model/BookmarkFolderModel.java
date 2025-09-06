package com.clucid.server.bookmark.entity.model;

import java.time.LocalDateTime;

import com.clucid.server.bookmark.persist.jpa.BookmarkFolderJpaEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class BookmarkFolderModel {
	private String id;
	private String name;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static BookmarkFolderModel fromJpa(BookmarkFolderJpaEntity jpa){
		return BookmarkFolderModel.builder()
			.id(jpa.getId())
			.name(jpa.getName())
			.createdAt(jpa.getCreatedAt())
			.updatedAt(jpa.getUpdatedAt())
			.build();
	}
}
