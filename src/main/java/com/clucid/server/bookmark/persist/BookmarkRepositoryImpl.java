package com.clucid.server.bookmark.persist;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.clucid.server.auth.persist.UserJpaEntity;
import com.clucid.server.auth.persist.UserJpaRepository;
import com.clucid.server.bookmark.entity.model.BookmarkFolderInModel;
import com.clucid.server.bookmark.entity.model.BookmarkFolderModel;
import com.clucid.server.bookmark.persist.jpa.BookmarkFolderJpaEntity;
import com.clucid.server.bookmark.persist.jpa.BookmarkFolderJpaRepository;
import com.clucid.server.bookmark.persist.jpa.BookmarkJpaEntity;
import com.clucid.server.bookmark.persist.jpa.BookmarkJpaRepository;
import com.clucid.server.bookmark.usecase.BookmarkRepository;
import com.clucid.server.terms.entity.model.TagOnlyNameModel;
import com.clucid.server.terms.entity.model.TermOnlyNameAndDefModel;
import com.clucid.server.terms.persist.jpa.TermJpaEntity;
import com.clucid.server.terms.persist.jpa.TermJpaRepository;
import com.clucid.server.terms.persist.jpa.TermTagJpaEntity;
import com.clucid.server.terms.persist.jpa.TermTagJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepository {

	private final UserJpaRepository userJpaRepository;
	private final TermJpaRepository termJpaRepository;
	private final TermTagJpaRepository termTagJpaRepository;
	private final BookmarkJpaRepository bookmarkJpaRepository;
	private final BookmarkFolderJpaRepository bookmarkFolderJpaRepository;

	@Override
	public void createBookmarkFolder(String name, String userId) {
		UserJpaEntity userJpa = userJpaRepository.findById(userId).orElseThrow();
		bookmarkFolderJpaRepository.save(
			BookmarkFolderJpaEntity.builder()
				.name(name)
				.user(userJpa)
				.build()
		);
	}

	@Override
	public List<BookmarkFolderModel> getBookmarkFolders(String userId) {
		UserJpaEntity userJpa = userJpaRepository.findById(userId).orElseThrow();
		List<BookmarkFolderJpaEntity> folderJpa = bookmarkFolderJpaRepository.findByUser(userJpa);
		return folderJpa.stream().map(BookmarkFolderModel::fromJpa).toList();
	}

	@Override
	public void createBookmark(String folderId, String termId, String userId) {
		UserJpaEntity userJpa = userJpaRepository.findById(userId).orElseThrow();
		BookmarkFolderJpaEntity folderJpa
			= bookmarkFolderJpaRepository.findByIdAndUser(folderId, userJpa).orElseThrow();
		TermJpaEntity termJpa = termJpaRepository.findById(termId).orElseThrow();
		bookmarkJpaRepository.save(
			BookmarkJpaEntity.builder()
				.folder(folderJpa)
				.term(termJpa)
				.user(userJpa)
				.build()
		);
	}

	@Override
	public void deleteBookmark(String folderId, String termId, String id) {
		UserJpaEntity userJpa = userJpaRepository.findById(id).orElseThrow();
		BookmarkFolderJpaEntity folderJpa
			= bookmarkFolderJpaRepository.findByIdAndUser(folderId, userJpa).orElseThrow();
		TermJpaEntity termJpa = termJpaRepository.findById(termId).orElseThrow();
		BookmarkJpaEntity bookmarkJpa
			= bookmarkJpaRepository.findByFolderAndTerm(folderJpa, termJpa).orElseThrow();
		bookmarkJpaRepository.delete(bookmarkJpa);
	}

	@Override
	public void deleteBookmarkFolder(String folderId, String userId) {
		UserJpaEntity userJpa = userJpaRepository.findById(userId).orElseThrow();
		BookmarkFolderJpaEntity folderJpa
			= bookmarkFolderJpaRepository.findByIdAndUser(folderId, userJpa).orElseThrow();
		bookmarkFolderJpaRepository.delete(folderJpa);
	}

	@Override
	public List<TermOnlyNameAndDefModel> getBookmarkByFolder(String folderId, String userId) {
		UserJpaEntity userJpa = userJpaRepository.findById(userId).orElseThrow();
		BookmarkFolderJpaEntity folderJpa
			= bookmarkFolderJpaRepository.findByIdAndUser(folderId, userJpa).orElseThrow();
		List<BookmarkJpaEntity> bookmarkJpa = bookmarkJpaRepository.findByFolder(folderJpa);
		List<TermJpaEntity> termJpaEntities = termJpaRepository.findAllById(
			bookmarkJpa.stream().map(b -> b.getTerm().getId()).toList()
		);
		List<TermTagJpaEntity> termTagJpaEntities = termTagJpaRepository.findAllByTermIn(termJpaEntities);
		Map<String, List<TermTagJpaEntity>> termTagMap = termTagJpaEntities.stream()
			.collect(Collectors.groupingBy(tt -> tt.getTerm().getId()));
		return termJpaEntities.stream()
			.map(tj -> {
				List<TermTagJpaEntity> tags = termTagMap.getOrDefault(tj.getId(), List.of());
				return TermOnlyNameAndDefModel.builder()
					.id(tj.getId())
					.nameKr(tj.getTermKorean())
					.definition(tj.getDefinition())
					.tags(tags.stream().map(TagOnlyNameModel::fromTermTagJpa).toList())
					.build();
			}).toList();
	}

	@Override
	public List<BookmarkFolderInModel> getBookmarkFolderIn(String termId, String id) {
		UserJpaEntity userJpa = userJpaRepository.findById(id).orElseThrow();
		TermJpaEntity termJpa = termJpaRepository.findById(termId).orElseThrow();
		List<BookmarkJpaEntity> bookmarkJpa = bookmarkJpaRepository.findByTermAndUser(termJpa, userJpa);
		Map<String,BookmarkJpaEntity> bookmarkMap = bookmarkJpa.stream()
			.collect(Collectors.toMap(BookmarkJpaEntity::getId, b -> b));
		List<BookmarkFolderJpaEntity> folderJpa = bookmarkFolderJpaRepository.findAllByUser(userJpa);
		List<BookmarkFolderInModel> folderInList = folderJpa.stream()
			.map(f ->{
				Boolean isIn = bookmarkMap.values().stream()
					.anyMatch(b -> b.getFolder().getId().equals(f.getId()));
				return BookmarkFolderInModel.fromJpaWithInfo(f,isIn);
			}).toList();
		return folderInList;
	}

	@Override
	public void updateBookmarkFolderName(String folderId, String name, String id) {
		UserJpaEntity userJpa = userJpaRepository.findById(id).orElseThrow();
		BookmarkFolderJpaEntity folderJpa
			= bookmarkFolderJpaRepository.findByIdAndUser(folderId, userJpa).orElseThrow();
		folderJpa.setName(name);
		bookmarkFolderJpaRepository.save(folderJpa);
	}
}
