package com.clucid.server.bookmark.entity.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CreateBookmarkRequest {
	private String folderId;
	private String termId;
}
