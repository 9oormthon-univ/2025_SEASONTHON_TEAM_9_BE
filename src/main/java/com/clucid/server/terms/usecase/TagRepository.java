package com.clucid.server.terms.usecase;

import java.util.List;

import com.clucid.server.terms.entity.model.TagModel;
import com.clucid.server.terms.entity.model.TermIdAndTagsModel;
import com.clucid.server.terms.entity.model.TermModel;

public interface TagRepository {
	String createTag(String tagName);

	void addTagsToTerm(List<String> tags, String termId);

	List<TagModel> getTagsByQuery(String query);

	List<TagModel> getAllTags();

	List<TermIdAndTagsModel> findTagsByTerms(List<TermModel> terms);
}
