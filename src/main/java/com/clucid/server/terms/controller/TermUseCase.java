package com.clucid.server.terms.controller;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.clucid.server.auth.entity.UserDetailsImpl;
import com.clucid.server.terms.entity.controller.RealCreateTermRequest;
import com.clucid.server.terms.entity.model.TermOnlyNameAndDefModel;
import com.clucid.server.terms.entity.model.TermWithTagAndRelModel;
import com.clucid.server.terms.entity.model.external.CreateTermResponse;
import com.clucid.server.terms.entity.model.external.CreateTermResult;
import com.clucid.server.terms.entity.usecase.CreateTermCommand;

public interface TermUseCase {
	void createTerm(CreateTermCommand fromRequest, String id);

	String uploadTermImage(MultipartFile image, UserDetailsImpl userDetails);

	List<TermWithTagAndRelModel> getTermsByIds(List<String> ids);

	List<TermOnlyNameAndDefModel> findAllNamesAndDefs();

	TermWithTagAndRelModel relateTermsById(String sourceTermId, String targetTermId);

	List<TermWithTagAndRelModel> getAllTerms();

	List<TermWithTagAndRelModel> findByNameContaining(String query);

	void markBookmarkedTerms(List<TermWithTagAndRelModel> responses, String id);

	void addTagsToTerm(String termId, String tagId);

	CreateTermResult test(String term);

	void createRealTerm(RealCreateTermRequest request);
}
