package com.clucid.server.bookmark.entity.controller;

import java.util.List;

import com.clucid.server.bookmark.entity.model.BookmarkFolderModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetBookmarkFoldersResponse {
	List<BookmarkFolderModel> folders;
}
