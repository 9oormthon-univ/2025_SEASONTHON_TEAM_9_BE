package com.clucid.server.bookmark.persist.jpa;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.clucid.server.auth.persist.UserJpaEntity;

@Entity
@Table(name = "bookmark_folders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkFolderJpaEntity {

	@Id
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserJpaEntity user;

	@Column(nullable = false, length = 255)
	private String name;

	@Column(name = "bookmark_count", nullable = false)
	private int bookmarkCount = 0;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@PrePersist
	public void prePersist() {
		if (id == null) id = java.util.UUID.randomUUID().toString();
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public void incrementBookmarkCount() {
		this.bookmarkCount++;
	}
	public void decrementBookmarkCount() {
		if (this.bookmarkCount > 0) {
			this.bookmarkCount--;
		}
	}
}
