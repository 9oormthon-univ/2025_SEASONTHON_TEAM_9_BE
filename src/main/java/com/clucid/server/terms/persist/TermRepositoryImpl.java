package com.clucid.server.terms.persist;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.clucid.server.auth.persist.UserJpaEntity;
import com.clucid.server.auth.persist.UserJpaRepository;
import com.clucid.server.bookmark.persist.jpa.BookmarkJpaEntity;
import com.clucid.server.bookmark.persist.jpa.BookmarkJpaRepository;
import com.clucid.server.terms.entity.model.TagOnlyNameModel;
import com.clucid.server.terms.entity.model.TermIdAndRelsModel;
import com.clucid.server.terms.entity.model.TermModel;
import com.clucid.server.terms.entity.model.TermOnlyNameAndDefModel;
import com.clucid.server.terms.persist.jpa.TagJpaEntity;
import com.clucid.server.terms.persist.jpa.TagJpaRepository;
import com.clucid.server.terms.persist.jpa.TermJpaEntity;
import com.clucid.server.terms.persist.jpa.TermJpaRepository;
import com.clucid.server.terms.persist.jpa.TermRelationJpaEntity;
import com.clucid.server.terms.persist.jpa.TermRelationJpaRepository;
import com.clucid.server.terms.persist.jpa.TermTagJpaEntity;
import com.clucid.server.terms.persist.jpa.TermTagJpaRepository;
import com.clucid.server.terms.usecase.TermRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TermRepositoryImpl implements TermRepository {
	private final UserJpaRepository userJpaRepository;
	private final TermJpaRepository termJpaRepository;
	private final TermRelationJpaRepository termRelationJpaRepository;

	private final TermTagJpaRepository termTagJpaRepository;
	private final BookmarkJpaRepository bookmarkJpaRepository;

	@Override
	public Optional<TermModel> findByNameKr(String nameKr) {
		return termJpaRepository.findByTermKorean(nameKr).map(TermModel::fromJpa);
	}

	@Override
	public TermModel saveTermOnly(TermModel term) {
		TermJpaEntity termJpa = termJpaRepository.save(TermModel.toJpa(term));
		return TermModel.fromJpa(termJpa);
	}

	@Override
	public Optional<TermModel> findById(String sourceTermId) {
		return termJpaRepository.findById(sourceTermId).map(TermModel::fromJpa);
	}

	@Override
	public List<TermModel> findAll() {
		return termJpaRepository.findAll().stream().map(TermModel::fromJpa).toList();
	}

	@Override
	public List<TermModel> findByNameContaining(String query) {
		return termJpaRepository.findByTermKoreanContainingIgnoreCase(query).stream().map(TermModel::fromJpa).toList();
	}

	@Override
	public Map<String, Boolean> findBookmarkedTermIdsByUserIdAndTermIds(String userId, List<String> termIds) {
		UserJpaEntity userJpa = userJpaRepository.findById(userId).get(); // 있으니까 온 거임
		List<TermJpaEntity> termJpaEntities = termJpaRepository.findAllById(termIds);
		// TODO : 원래는 Aggregate 조회인데, 나중에 합시다.. ㅋㅋ
		List<BookmarkJpaEntity> bookmarkJpaEntities
			= bookmarkJpaRepository.findByUserAndTermIdIn(userJpa, termIds);
		Set<String> bookmarkedTermIdSet = bookmarkJpaEntities.stream()
			.map(b -> b.getTerm().getId())
			.collect(Collectors.toSet());
		Map<String, Boolean> result = termJpaEntities.stream()
			.collect(Collectors.toMap(
				TermJpaEntity::getId,
				tj -> bookmarkedTermIdSet.contains(tj.getId())
			));
		return result;
	}

	@Override
	public List<TermModel> findAllByIds(List<String> ids) {
		List<TermJpaEntity> termJpaEntities = termJpaRepository.findAllById(ids);
		return termJpaEntities.stream().map(TermModel::fromJpa).toList();
	}

	@Override
	public List<TermIdAndRelsModel> findRelsByTerms(List<TermModel> terms) {
		List<String> termIds = terms.stream().map(TermModel::getId).toList();
		List<TermJpaEntity> termJpaEntities = termJpaRepository.findAllById(termIds);
		List<TermRelationJpaEntity> termRelationJpaEntities = termRelationJpaRepository.findByTermIn(termJpaEntities);
		Set<String> relatedTermIdSet = termRelationJpaEntities.stream()
			.map(tr -> tr.getRelatedTerm().getId())
			.collect(Collectors.toSet());
		Map<String, List<TermRelationJpaEntity>> termRelMap = termRelationJpaEntities.stream()
			.collect(Collectors.groupingBy(tr -> tr.getTerm().getId()));

		termJpaRepository.findAllById(relatedTermIdSet); // 영속성 컨텍스트에 로드

		List<TermTagJpaEntity> termTagJpaEntities
			= termTagJpaRepository.findByTermIdIn(relatedTermIdSet.stream().toList());
		Map<String, List<TermTagJpaEntity>> relTermTagMap = termTagJpaEntities.stream()
			.collect(Collectors.groupingBy(tt -> tt.getTerm().getId()));

		List<TermIdAndRelsModel> result = terms.stream().map(term -> {
			List<TermOnlyNameAndDefModel> relatedTermList
				= termRelMap.getOrDefault(term.getId(), new ArrayList<>()).stream()
				.map(tr -> {
					String relTermId = tr.getRelatedTerm().getId();
					List<TermTagJpaEntity> relTermTags = relTermTagMap.getOrDefault(relTermId, new ArrayList<>());
					return TermOnlyNameAndDefModel.builder()
						.id(tr.getRelatedTerm().getId())
						.nameKr(tr.getRelatedTerm().getTermKorean())
						.definition(tr.getRelatedTerm().getDefinition()) // 아마 영속성 컨텍스트에 넣어서 추가 쿼리는 안날라갈듯
						.tags(relTermTags.stream().map(TagOnlyNameModel::fromTermTagJpa).toList())
						.build();
				})
				.collect(Collectors.toList());
			return TermIdAndRelsModel.builder()
				.id(term.getId())
				.relatedTerms(relatedTermList)
				.build();
		}).collect(Collectors.toList());

		return result;
	}

	@Override
	public List<TermOnlyNameAndDefModel> findAllNamesAndDefs() {
		List<TermJpaEntity> termJpaEntities = termJpaRepository.findAll();
		List<TermTagJpaEntity> termTagJpaEntities = termTagJpaRepository.findByTermIn(termJpaEntities);
		Map<String, List<TermTagJpaEntity>> termTagMap = termTagJpaEntities.stream()
			.collect(Collectors.groupingBy(tt -> tt.getTerm().getId()));

		return termJpaEntities.stream().map(tj -> TermOnlyNameAndDefModel.builder()
			.id(tj.getId())
			.nameKr(tj.getTermKorean())
			.definition(tj.getDefinition())
			.tags(termTagMap.getOrDefault(tj.getId(), List.of()).stream()
				.map(TagOnlyNameModel::fromTermTagJpa).toList())
			.build()).toList();
	}

	@Override
	public void relateTermsById(TermModel sourceTerm, TermModel targetTerm) {
		TermJpaEntity sourceTermJpa = termJpaRepository.findById(sourceTerm.getId()).get();
		TermJpaEntity targetTermJpa = termJpaRepository.findById(targetTerm.getId()).get();//있으니까 온 거임
		TermRelationJpaEntity newRelation = TermRelationJpaEntity.builder()
			.term(sourceTermJpa)
			.relatedTerm(targetTermJpa)
			.build();
		termRelationJpaRepository.save(newRelation);
	}

}
	/*
	* 	@Override
	public List<TermIdAndTagsModel> findTagsByTerms(List<TermModel> terms) {
		List<String> termIds = terms.stream().map(TermModel::getId).toList();
		List<TermJpaEntity> termJpaEntities = termJpaRepository.findAllById(termIds);
		List<TermTagJpaEntity> termTagJpaEntities = termTagJpaRepository.findByTermIn(termJpaEntities);
		Map<String, List<TermTagJpaEntity>> termTagMap = new HashMap<>();
		termTagJpaEntities.stream().forEach(tt ->{
			String key = tt.getTerm().getId();
			termTagMap.putIfAbsent(key,  new ArrayList<>());
			termTagMap.get(key).add(tt);
		});
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
	}*/
