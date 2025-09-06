package com.clucid.server.terms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clucid.server.auth.entity.UserDetailsImpl;
import com.clucid.server.terms.entity.controller.CreateTagRequest;
import com.clucid.server.terms.entity.controller.GetTagsResponse;
import com.clucid.server.terms.entity.model.TagModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TagController {
	private final TagUseCase tagUseCase;

	@Operation(summary = "모든 태그 조회. 프론트에서 검색해주시죠")
	@GetMapping("/api/tags")
	public ResponseEntity<GetTagsResponse> getTags(@RequestParam(value = "query", required = false) String query){
		List<TagModel> tagsByQuery = tagUseCase.getTagsByQuery(query);
		return ResponseEntity.ok(GetTagsResponse.fromModelList(tagsByQuery));
	}

	@Operation(summary = "태그 생성", security = { @SecurityRequirement(name = "JWT") })
	@PostMapping("/api/tags")
	public ResponseEntity<Void> createTag(@RequestBody CreateTagRequest request){
		tagUseCase.createTag(request.getTagName());
		return ResponseEntity.ok().build();
	}
}
