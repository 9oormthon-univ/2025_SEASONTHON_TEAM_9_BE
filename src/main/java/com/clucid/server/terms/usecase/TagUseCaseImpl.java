package com.clucid.server.terms.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clucid.server.terms.controller.TagUseCase;
import com.clucid.server.terms.entity.model.TagModel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagUseCaseImpl implements TagUseCase {
	private final TagRepository tagRepository;
	@Override
	@Transactional
	public String createTag(String tagName) {
		return tagRepository.createTag(tagName);
	}

	@Override
	public List<TagModel> getTagsByQuery(String query) {
		if(query != null && !query.isEmpty()){
			return tagRepository.getTagsByQuery(query);
		}else{
			return tagRepository.getAllTags();
		}
	}
}
