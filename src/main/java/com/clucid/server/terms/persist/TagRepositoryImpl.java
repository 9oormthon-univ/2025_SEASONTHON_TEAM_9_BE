package com.clucid.server.terms.persist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.clucid.server.terms.entity.model.TagModel;
import com.clucid.server.terms.entity.model.TagOnlyNameModel;
import com.clucid.server.terms.entity.model.TermIdAndTagsModel;
import com.clucid.server.terms.entity.model.TermModel;
import com.clucid.server.terms.persist.jpa.TagJpaEntity;
import com.clucid.server.terms.persist.jpa.TagJpaRepository;
import com.clucid.server.terms.persist.jpa.TermJpaEntity;
import com.clucid.server.terms.persist.jpa.TermJpaRepository;
import com.clucid.server.terms.persist.jpa.TermTagJpaEntity;
import com.clucid.server.terms.persist.jpa.TermTagJpaRepository;
import com.clucid.server.terms.usecase.TagRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
	private final TermJpaRepository termJpaRepository;
	private final TagJpaRepository tagJpaRepository;
	private final TermTagJpaRepository termTagJpaRepository;

	@Override
	public String createTag(String tagName) {
		TagJpaEntity tagJpaEntity = TagJpaEntity.builder().name(tagName).build();
		tagJpaEntity = tagJpaRepository.save(tagJpaEntity);
		return tagJpaEntity.getId();
	}

	@Override
	public void addTagsToTerm(List<String> tags, String termId) {
		TermJpaEntity termJpaEntity = termJpaRepository.findById(termId).get(); // 이미 있으니 여기 왔기에, get 해도 됨
		List<TagJpaEntity> tagJpaEntities = tagJpaRepository.findAllById(tags);
		tagJpaEntities.forEach(TagJpaEntity::incrementRefCount);
		List<TermTagJpaEntity> termTagJpaEntities = tagJpaEntities.stream()
			.map(tag -> TermTagJpaEntity.builder().term(termJpaEntity).tag(tag).tagName(tag.getName()).build())
			.toList();
		termTagJpaRepository.saveAll(termTagJpaEntities);
	}

	@Override
	public List<TagModel> getTagsByQuery(String query) {
		List<TagJpaEntity> tagJpaEntities = tagJpaRepository.findByNameContainingIgnoreCase(query);
		return tagJpaEntities.stream()
			.map(tag -> TagModel.builder().id(tag.getId()).name(tag.getName()).refCount(tag.getRefCount()).build())
			.toList();
	}

	@Override
	public List<TagModel> getAllTags() {
		List<TagJpaEntity> tagJpaEntities = tagJpaRepository.findAll();
		return tagJpaEntities.stream()
			.map(tag -> TagModel.builder().id(tag.getId()).name(tag.getName()).refCount(tag.getRefCount()).build())
			.toList();
	}

	@Override
	public List<TermIdAndTagsModel> findTagsByTerms(List<TermModel> terms) {
		List<String> termIds = terms.stream().map(TermModel::getId).toList();
		List<TermJpaEntity> termJpaEntities = termJpaRepository.findAllById(termIds);
		List<TermTagJpaEntity> termTagJpaEntities = termTagJpaRepository.findByTermIn(termJpaEntities);
		Map<String, List<TermTagJpaEntity>> termTagMap = termTagJpaEntities.stream()
			.collect(Collectors.groupingBy(tr -> tr.getTerm().getId()));

		List<TermIdAndTagsModel> result = new ArrayList<>();
		for (TermModel term : terms) {
			List<TagOnlyNameModel> tags = new ArrayList<>();
			if(termTagMap.containsKey(term.getId())) {
				tags = termTagMap.get(term.getId()).stream()
					.map(tt -> TagOnlyNameModel.builder()
						.id(tt.getTag().getId())
						.name(tt.getTag().getName())
						.build())
					.toList();
			}
			result.add(TermIdAndTagsModel.builder().termId(term.getId()).tags(tags).build());
		}
		return result;
	}
}
