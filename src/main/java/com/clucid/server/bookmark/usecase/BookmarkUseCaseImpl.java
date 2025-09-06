package com.clucid.server.bookmark.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clucid.server.auth.entity.UserDetailsImpl;
import com.clucid.server.bookmark.controller.BookmarkUseCase;
import com.clucid.server.bookmark.entity.model.BookmarkFolderInModel;
import com.clucid.server.bookmark.entity.model.BookmarkFolderModel;
import com.clucid.server.terms.entity.model.TermOnlyNameAndDefModel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkUseCaseImpl implements BookmarkUseCase {
	private final BookmarkRepository bookmarkRepository;
	@Override
	@Transactional
	public void createBookmarkFolder(String name, UserDetailsImpl userDetails) {
		bookmarkRepository.createBookmarkFolder(name, userDetails.getId());
	}

	@Override
	public List<BookmarkFolderModel> getBookmarkFolders(UserDetailsImpl userDetails) {
		return bookmarkRepository.getBookmarkFolders(userDetails.getId());
	}

	@Override
	@Transactional
	public void createBookmark(String folderId, String termId, UserDetailsImpl userDetails) {
		bookmarkRepository.createBookmark(folderId, termId,userDetails.getId());
	}

	@Override
	@Transactional
	public void deleteBookmark(String folderId, String termId, UserDetailsImpl userDetails) {
		bookmarkRepository.deleteBookmark(folderId, termId,userDetails.getId());
	}

	@Override
	public void deleteBookmarkFolder(String folderId, UserDetailsImpl userDetails) {
		bookmarkRepository.deleteBookmarkFolder(folderId, userDetails.getId());
	}

	@Override
	public List<TermOnlyNameAndDefModel> getBookmarksByFolder(String folderId, UserDetailsImpl userDetails) {
		return bookmarkRepository.getBookmarkByFolder(folderId,userDetails.getId());
	}

	@Override
	public List<BookmarkFolderInModel> getBookmarkFolderIn(String termId, UserDetailsImpl userDetails) {
		return bookmarkRepository.getBookmarkFolderIn(termId,userDetails.getId());
	}

	@Override
	public void updateBookmarkFolderName(String folderId, String name, UserDetailsImpl userDetails) {
		bookmarkRepository.updateBookmarkFolderName(folderId, name, userDetails.getId());
	}
}
