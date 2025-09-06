package com.clucid.server.bookmark.controller;

import java.util.List;

import com.clucid.server.auth.entity.UserDetailsImpl;
import com.clucid.server.bookmark.entity.model.BookmarkFolderInModel;
import com.clucid.server.bookmark.entity.model.BookmarkFolderModel;
import com.clucid.server.terms.entity.model.TermOnlyNameAndDefModel;

public interface BookmarkUseCase {
	void createBookmarkFolder(String name, UserDetailsImpl userDetails);

	List<BookmarkFolderModel> getBookmarkFolders(UserDetailsImpl userDetails);

	void createBookmark(String folderId, String termId,UserDetailsImpl userDetails);

	void deleteBookmark(String folderId, String termId, UserDetailsImpl userDetails);

	void deleteBookmarkFolder(String folderId, UserDetailsImpl userDetails);

	List<TermOnlyNameAndDefModel> getBookmarksByFolder(String folderId, UserDetailsImpl userDetails);

	List<BookmarkFolderInModel> getBookmarkFolderIn(String termId, UserDetailsImpl userDetails);

	void updateBookmarkFolderName(String folderId, String name, UserDetailsImpl userDetails);
}
