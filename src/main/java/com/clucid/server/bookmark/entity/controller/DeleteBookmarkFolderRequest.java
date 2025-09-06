package com.clucid.server.bookmark.entity.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class DeleteBookmarkFolderRequest {
	private String folderId;
}
