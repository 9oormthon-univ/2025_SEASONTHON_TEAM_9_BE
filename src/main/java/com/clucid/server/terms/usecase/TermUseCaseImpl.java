package com.clucid.server.terms.usecase;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.clucid.server.auth.entity.UserDetailsImpl;
import com.clucid.server.infra.s3.FileNameConstructor;
import com.clucid.server.infra.s3.ObjectStorageSender;
import com.clucid.server.terms.controller.TermUseCase;
import com.clucid.server.terms.entity.model.TagOnlyNameModel;
import com.clucid.server.terms.entity.model.TermIdAndRelsModel;
import com.clucid.server.terms.entity.model.TermIdAndTagsModel;
import com.clucid.server.terms.entity.model.TermModel;
import com.clucid.server.terms.entity.model.TermOnlyNameAndDefModel;
import com.clucid.server.terms.entity.model.TermWithTagAndRelModel;
import com.clucid.server.terms.entity.usecase.CreateTermCommand;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TermUseCaseImpl implements TermUseCase {
	private final TermRepository termRepository;
	private final TagRepository tagRepository;

	private final ObjectStorageSender objectStorageSender;

	@Override
	@Transactional
	public void createTerm(CreateTermCommand request, String id) {
		if (termRepository.findByNameKr(request.getNameKr()).isPresent()) {
			throw new IllegalArgumentException("Term already exists");
		}
		TermModel term = TermModel.builder()
			.nameKr(request.getNameKr())
			.nameEn(request.getNameEn())
			.definition(request.getDefinition())
			.imgUrl(request.getImgUrl())
			.build();
		term = termRepository.saveTermOnly(term);
		if (request.getTags() != null && !request.getTags().isEmpty()) {
			tagRepository.addTagsToTerm(request.getTags(), term.getId());
		}
		// TODO : 벡터 DB 에 넣기
	}

	@Override
	public String uploadTermImage(MultipartFile image, UserDetailsImpl userDetails) {
		String fileName = FileNameConstructor.constructTermImageFileName(userDetails.getId(), image);
		return objectStorageSender.sendImage(fileName, image);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TermWithTagAndRelModel> getTermsByIds(List<String> ids) {
		List<TermModel> terms = termRepository.findAllByIds(ids);
		return convertToTermWithTagAndRelModels(terms);
	}

	@Override
	public List<TermOnlyNameAndDefModel> findAllNamesAndDefs() {
		return termRepository.findAllNamesAndDefs();
	}

	@Override
	@Transactional
	public TermWithTagAndRelModel relateTermsById(String sourceTermId, String targetTermId) {
		TermModel sourceTerm = termRepository.findById(sourceTermId)
			.orElseThrow(() -> new IllegalArgumentException("Source term not found"));
		TermModel targetTerm = termRepository.findById(targetTermId)
			.orElseThrow(() -> new IllegalArgumentException("Target term not found"));
		termRepository.relateTermsById(sourceTerm, targetTerm);
		TermIdAndTagsModel sourceTermWithTags = tagRepository.findTagsByTerms(List.of(sourceTerm)).get(0);
		List<TagOnlyNameModel> tags = sourceTermWithTags.getTags();
		List<TermOnlyNameAndDefModel> relations = termRepository.findRelsByTerms(List.of(sourceTerm)).get(0)
			.getRelatedTerms();
		return TermWithTagAndRelModel.builder()
			.id(sourceTerm.getId())
			.nameKr(sourceTerm.getNameKr())
			.nameEn(sourceTerm.getNameEn())
			.definition(sourceTerm.getDefinition())
			.imgUrl(sourceTerm.getImgUrl())
			.tags(tags)
			.relations(relations)
			.build();
	}

	@Override
	public List<TermWithTagAndRelModel> getAllTerms() {
		List<TermModel> terms = termRepository.findAll();
		return convertToTermWithTagAndRelModels(terms);
	}

	@Override
	public List<TermWithTagAndRelModel> findByNameContaining(String query) {
		List<TermModel> terms = termRepository.findByNameContaining(query);
		return convertToTermWithTagAndRelModels(terms);
	}

	@Override
	public void markBookmarkedTerms(List<TermWithTagAndRelModel> responses, String userId) {
		List<String> termIds = responses.stream().map(TermWithTagAndRelModel::getId).collect(Collectors.toList());
		Map<String, Boolean> bookmarkedTermIds = termRepository.findBookmarkedTermIdsByUserIdAndTermIds(userId, termIds);
		responses.forEach(response -> {
			response.setIsBookmarked(bookmarkedTermIds.getOrDefault(response.getId(), false));
		});
	}

	private List<TermWithTagAndRelModel> convertToTermWithTagAndRelModels(List<TermModel> terms) {

		List<TermIdAndTagsModel> termsWithTags = tagRepository.findTagsByTerms(terms);
		Map<String,List<TagOnlyNameModel>> termIdToTags = termsWithTags.stream()
			.collect(Collectors.toMap(
				TermIdAndTagsModel::getTermId,
				TermIdAndTagsModel::getTags
			));

		List<TermIdAndRelsModel> termsWithRels = termRepository.findRelsByTerms(terms);
		Map<String, List<TermOnlyNameAndDefModel>> termIdToRels = termsWithRels.stream()
			.collect(Collectors.toMap(
				TermIdAndRelsModel::getId,
				TermIdAndRelsModel::getRelatedTerms
			));

		return terms.stream().map(term -> TermWithTagAndRelModel.builder()
			.id(term.getId())
			.nameKr(term.getNameKr())
			.nameEn(term.getNameEn())
			.definition(term.getDefinition())
			.imgUrl(term.getImgUrl())
			.tags(termIdToTags.getOrDefault(term.getId(), List.of()))
			.relations(termIdToRels.getOrDefault(term.getId(), List.of()))
			.build()
		).collect(Collectors.toList());
	}
}
