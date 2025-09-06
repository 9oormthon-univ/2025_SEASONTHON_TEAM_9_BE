package com.clucid.server.bookmark.usecase;

import java.util.List;

import com.clucid.server.bookmark.entity.model.BookmarkFolderInModel;
import com.clucid.server.bookmark.entity.model.BookmarkFolderModel;
import com.clucid.server.terms.entity.model.TermOnlyNameAndDefModel;

public interface BookmarkRepository {
	void createBookmarkFolder(String name, String id);

	List<BookmarkFolderModel> getBookmarkFolders(String id);

	void createBookmark(String folderId, String termId, String userId);

	void deleteBookmark(String folderId, String termId, String id);

	void deleteBookmarkFolder(String folderId, String userId);

	List<TermOnlyNameAndDefModel> getBookmarkByFolder(String folderId, String id);

	List<BookmarkFolderInModel> getBookmarkFolderIn(String termId, String id);

	void updateBookmarkFolderName(String folderId, String name, String id);
}
