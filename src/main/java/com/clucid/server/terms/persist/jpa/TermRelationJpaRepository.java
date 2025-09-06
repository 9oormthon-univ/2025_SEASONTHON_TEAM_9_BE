package com.clucid.server.terms.persist.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRelationJpaRepository extends JpaRepository<TermRelationJpaEntity, String> {
	List<TermRelationJpaEntity> findByTermIn(List<TermJpaEntity> termJpaEntities);
}
