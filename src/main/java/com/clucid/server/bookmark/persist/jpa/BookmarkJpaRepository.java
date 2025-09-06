package com.clucid.server.bookmark.persist.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.clucid.server.auth.persist.UserJpaEntity;
import com.clucid.server.terms.persist.jpa.TermJpaEntity;

public interface BookmarkJpaRepository extends JpaRepository<BookmarkJpaEntity,String> {

	Optional<BookmarkJpaEntity> findByFolderAndTerm(BookmarkFolderJpaEntity folderJpa, TermJpaEntity termJpa);

	List<BookmarkJpaEntity> findByFolder(BookmarkFolderJpaEntity folderJpa);

	List<BookmarkJpaEntity> findByTermAndUser(TermJpaEntity termJpa, UserJpaEntity userJpa);

	@Query("SELECT b FROM BookmarkJpaEntity b WHERE b.user = :userJpa AND b.term.id IN :termIds")
	List<BookmarkJpaEntity> findByUserAndTermIdIn(UserJpaEntity userJpa, List<String> termIds);
}
