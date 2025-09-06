package com.clucid.server.terms.usecase;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.clucid.server.terms.entity.model.TermIdAndRelsModel;
import com.clucid.server.terms.entity.model.TermModel;
import com.clucid.server.terms.entity.model.TermOnlyNameAndDefModel;

public interface TermRepository {
	Optional<TermModel> findByNameKr(String nameKr);

	TermModel saveTermOnly(TermModel term);

	List<TermModel> findAllByIds(List<String> ids);

	List<TermIdAndRelsModel> findRelsByTerms(List<TermModel> terms);
	List<TermOnlyNameAndDefModel> findAllNamesAndDefs();

	void relateTermsById(TermModel sourceTermId, TermModel targetTermId);

	Optional<TermModel> findById(String sourceTermId);

	List<TermModel> findAll();

	List<TermModel> findByNameContaining(String query);

	Map<String, Boolean> findBookmarkedTermIdsByUserIdAndTermIds(String userId, List<String> termIds);
}
