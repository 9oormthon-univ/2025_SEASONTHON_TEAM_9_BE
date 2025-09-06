package com.clucid.server.terms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.clucid.server.terms.entity.controller.TermPageParam;
import com.clucid.server.auth.entity.UserDetailsImpl;
import com.clucid.server.terms.entity.controller.CreateTermRequest;
import com.clucid.server.terms.entity.controller.GetOneTermResponse;
import com.clucid.server.terms.entity.controller.GetTermsResponse;
import com.clucid.server.terms.entity.controller.RelateTermsRequest;
import com.clucid.server.terms.entity.controller.UploadTermImageResponse;
import com.clucid.server.terms.entity.model.TermOnlyNameAndDefModel;
import com.clucid.server.terms.entity.model.TermWithTagAndRelModel;
import com.clucid.server.terms.entity.usecase.CreateTermCommand;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TermController {
	private final TermUseCase termUseCase;

	@Operation(summary = "특정 용어들 조회 (ids 파라미터가 없으면 전체 조회). 추후 필터, 페이징 예정", security = {
		@SecurityRequirement(name = "JWT")})
	@GetMapping("/api/terms")
	public ResponseEntity<GetTermsResponse> getTermsByIds(
		@RequestParam(required = false) List<String> ids
		, @ModelAttribute TermPageParam pageParam
		, @AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		List<TermWithTagAndRelModel> responses;
		if (ids == null || ids.isEmpty()) {
			responses = termUseCase.getAllTerms();
		} else {
			responses = termUseCase.getTermsByIds(ids);
		}
		if (userDetails != null) {
			termUseCase.markBookmarkedTerms(responses, userDetails.getId());
		}
		return ResponseEntity.ok().body(GetTermsResponse.fromModel(responses));
	}

	@Operation(summary = "모든 용어 조회. 추후 필터, 페이징 예정", security = {@SecurityRequirement(name = "JWT")})
	@GetMapping("/api/terms/all")
	public ResponseEntity<GetTermsResponse> getAllTerms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		List<TermWithTagAndRelModel> responses = termUseCase.getAllTerms();
		if (userDetails != null) {
			termUseCase.markBookmarkedTerms(responses, userDetails.getId());
		}
		return ResponseEntity.ok().body(GetTermsResponse.fromModel(responses));
	}

	@Operation(summary = "이름 통한 검색. 추후 필터, 페이징 예정", security = {@SecurityRequirement(name = "JWT")})
	@GetMapping("/api/terms/names")
	public ResponseEntity<GetTermsResponse> getTermNamesByQuery(@RequestParam String query) {
		return ResponseEntity.ok().body(GetTermsResponse.fromModel(termUseCase.findByNameContaining(query)));
	}

	@Operation(summary = "모든 용어의 이름, 정의, 태그까지 조회. 프론트에서 받아놓고 검색용", security = {@SecurityRequirement(name = "JWT")})
	@GetMapping("/api/terms/all/names")
	public ResponseEntity<List<TermOnlyNameAndDefModel>> getTermNamesByQuery() {
		return ResponseEntity.ok().body(termUseCase.findAllNamesAndDefs());
	}

	@Operation(summary = "용어 생성, example 은 내부에서 생성.(example 생성은 아직 구현 안됨)"
		, security = {@SecurityRequirement(name = "JWT")})
	@PostMapping("/api/terms")
	public ResponseEntity<Void> createTerm(
		@RequestBody CreateTermRequest request
		, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		termUseCase.createTerm(CreateTermCommand.fromRequest(request), userDetails.getId());
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "용어 이미지 업로드. 이후에 이 string imgUrl 그대로 create term 의 request 로 넣기 "
		, security = {@SecurityRequirement(name = "JWT")})
	@PostMapping("/api/terms/images")
	public ResponseEntity<UploadTermImageResponse> uploadTruckImage
		(MultipartFile image,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		String result = termUseCase.uploadTermImage(image, userDetails);
		return ResponseEntity.ok(new UploadTermImageResponse(result));
	}

	@Operation(summary = "용어들 관계 맺기. 현재는 직접 연관. 추후 배치처리로 연관 한번에 업데이트 예정", security = {
		@SecurityRequirement(name = "JWT")})
	@PostMapping("/api/terms/relations")
	public ResponseEntity<GetOneTermResponse> relateTermsById(@RequestBody RelateTermsRequest request) {
		TermWithTagAndRelModel termWithTagAndRelModel = termUseCase.relateTermsById(request.getSourceTermId(),
			request.getTargetTermId());
		return ResponseEntity.ok(GetOneTermResponse.fromModel(termWithTagAndRelModel));
	}
}
