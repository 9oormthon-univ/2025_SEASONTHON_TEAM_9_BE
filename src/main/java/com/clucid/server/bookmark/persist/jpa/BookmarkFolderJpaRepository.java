package com.clucid.server.bookmark.persist.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clucid.server.auth.persist.UserJpaEntity;

public interface BookmarkFolderJpaRepository extends JpaRepository<BookmarkFolderJpaEntity, String> {
	List<BookmarkFolderJpaEntity> findByUser(UserJpaEntity userJpa);

	Optional<BookmarkFolderJpaEntity> findByIdAndUser(String folderId, UserJpaEntity userJpa);

	List<BookmarkFolderJpaEntity> findAllByUser(UserJpaEntity userJpa);
}
