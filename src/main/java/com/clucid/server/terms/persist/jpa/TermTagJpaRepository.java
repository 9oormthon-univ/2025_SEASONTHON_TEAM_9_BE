package com.clucid.server.terms.persist.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TermTagJpaRepository extends JpaRepository<TermTagJpaEntity, String> {
	List<TermTagJpaEntity> findByTermIn(List<TermJpaEntity> termJpaEntities);
	List<TermTagJpaEntity> findByTermIdIn(List<String> termIds);

	List<TermTagJpaEntity> findAllByTermIn(List<TermJpaEntity> termJpaEntities);
}
