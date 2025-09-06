package com.clucid.server.bookmark.persist.jpa;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.clucid.server.auth.persist.UserJpaEntity;
import com.clucid.server.terms.persist.jpa.TermJpaEntity;

@Entity
@Table(name = "bookmarks",
	uniqueConstraints = {@UniqueConstraint(columnNames = {"folder_id", "term_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkJpaEntity {

	@Id
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "folder_id", nullable = false)
	private BookmarkFolderJpaEntity folder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "term_id", nullable = false)
	private TermJpaEntity term;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserJpaEntity user;


	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {
		if (id == null) id = java.util.UUID.randomUUID().toString();
		createdAt = LocalDateTime.now();
	}
}
