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

	@Builder.Default
	private int bookmarkCount = 0; // 북마크 폴더 내 북마크 개수

	public static BookmarkFolderModel fromJpa(BookmarkFolderJpaEntity jpa) {
		return BookmarkFolderModel.builder()
			.id(jpa.getId())
			.name(jpa.getName())
			.createdAt(jpa.getCreatedAt())
			.updatedAt(jpa.getUpdatedAt())
			.build();
	}

	public static BookmarkFolderModel fromJpaWithCount(BookmarkFolderJpaEntity jpa, int bookmarkCount) {
		return BookmarkFolderModel.builder()
			.id(jpa.getId())
			.name(jpa.getName())
			.bookmarkCount(bookmarkCount)
			.createdAt(jpa.getCreatedAt())
			.updatedAt(jpa.getUpdatedAt())
			.build();
	}
}
