package com.clucid.server.terms.persist.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TermJpaRepository extends JpaRepository<TermJpaEntity,String> {

	Optional<TermJpaEntity> findByTermKorean(String nameKr);

	@Query("SELECT t FROM TermJpaEntity t WHERE LOWER(t.termKorean) LIKE LOWER(CONCAT('%', :query, '%'))")
	List<TermJpaEntity> findByTermKoreanContainingIgnoreCase(String query);
}
