package com.clucid.server.bookmark.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clucid.server.auth.entity.UserDetailsImpl;
import com.clucid.server.bookmark.entity.controller.CreateBookmarkFolderRequest;
import com.clucid.server.bookmark.entity.controller.CreateBookmarkRequest;
import com.clucid.server.bookmark.entity.controller.DeleteBookmarkFolderRequest;
import com.clucid.server.bookmark.entity.controller.GetBookmarkFolderInRequest;
import com.clucid.server.bookmark.entity.controller.GetBookmarkFoldersResponse;
import com.clucid.server.bookmark.entity.controller.GetBookmarksResponse;
import com.clucid.server.bookmark.entity.model.BookmarkFolderInModel;
import com.clucid.server.bookmark.entity.model.BookmarkFolderModel;
import com.clucid.server.terms.entity.model.TermOnlyNameAndDefModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookmarkController {
	private final BookmarkUseCase bookmarkUseCase;
	
	@Operation(summary = "내 북마크 폴더 조회", security = { @SecurityRequirement(name = "JWT") })
	@GetMapping("/api/bookmarks/folders")
	public ResponseEntity<GetBookmarkFoldersResponse> getBookmarkFolders(
		@AuthenticationPrincipal UserDetailsImpl userDetails){
		List<BookmarkFolderModel> folderModelList = bookmarkUseCase.getBookmarkFolders(userDetails);
		return ResponseEntity.ok(new GetBookmarkFoldersResponse(folderModelList));
	}

	@Operation(summary = "북마크 폴더 생성", security = { @SecurityRequirement(name = "JWT") })
	@PostMapping("/api/bookmarks/folders")
	public ResponseEntity<Void> createBookmarkFolder(
		@RequestBody CreateBookmarkFolderRequest request,
		@AuthenticationPrincipal UserDetailsImpl userDetails){
		bookmarkUseCase.createBookmarkFolder(request.getName(), userDetails);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "북마크 폴더 삭제", security = { @SecurityRequirement(name = "JWT") })
	@DeleteMapping("/api/bookmarks/folders/delete")
	public ResponseEntity<Void> deleteBookmarkFolder(
		@RequestBody DeleteBookmarkFolderRequest request,
		@AuthenticationPrincipal UserDetailsImpl userDetails){
		bookmarkUseCase.deleteBookmarkFolder(request.getFolderId(), userDetails);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "폴더 내 북마크 생성. 그냥 뭐 지정안하면 (DEFAULT) 라는 폴더를 만들죠?"
		, security = { @SecurityRequirement(name = "JWT") })
	@PostMapping("/api/bookmarks")
	public ResponseEntity<Void> createBookmark(
		@RequestBody CreateBookmarkRequest request,
		@AuthenticationPrincipal UserDetailsImpl userDetails){
		bookmarkUseCase.createBookmark(request.getFolderId(),request.getTermId(), userDetails);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "북마크된 TERM의 폴더 위치", security = { @SecurityRequirement(name = "JWT") })
	@GetMapping("/api/bookmarks/folder/in")
	public ResponseEntity<List<BookmarkFolderInModel>> getBookmarkFolderIn(
		@RequestParam String termId,
		@AuthenticationPrincipal UserDetailsImpl userDetails){
		List<BookmarkFolderInModel> folderIdList = bookmarkUseCase.getBookmarkFolderIn(termId, userDetails);
		return ResponseEntity.ok(folderIdList);
	}


	@Operation(summary = "폴더 내 북마크 조회", security = { @SecurityRequirement(name = "JWT") })
	@GetMapping("/api/bookmarks")
	public ResponseEntity<GetBookmarksResponse> getBookmarks(
		@RequestParam String folderId,
		@AuthenticationPrincipal UserDetailsImpl userDetails){
		List<TermOnlyNameAndDefModel> termIdList = bookmarkUseCase.getBookmarksByFolder(folderId, userDetails);
		return ResponseEntity.ok(new GetBookmarksResponse(termIdList));
	}

	@Operation(summary = "북마크 삭제", security = { @SecurityRequirement(name = "JWT") })
	@DeleteMapping("/api/bookmarks/delete")
	public ResponseEntity<Void> deleteBookmark(
		@RequestBody CreateBookmarkRequest request,
		@AuthenticationPrincipal UserDetailsImpl userDetails){
		bookmarkUseCase.deleteBookmark(request.getFolderId(),request.getTermId(), userDetails);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "북마크 폴더 이름 변경", security = { @SecurityRequirement(name = "JWT") })
	@PatchMapping("/api/bookmarks/folders/{folderId}")
	public ResponseEntity<Void> updateBookmarkFolderName(
		@PathVariable String folderId,
		@RequestBody CreateBookmarkFolderRequest request,
		@AuthenticationPrincipal UserDetailsImpl userDetails){
		bookmarkUseCase.updateBookmarkFolderName(folderId,request.getName(), userDetails);
		return ResponseEntity.ok().build();
	}


}
