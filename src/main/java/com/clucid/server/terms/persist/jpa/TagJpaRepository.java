package com.clucid.server.terms.persist.jpa;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagJpaRepository extends JpaRepository<TagJpaEntity, String> {
	@Query("SELECT t FROM TagJpaEntity t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%'))")
	List<TagJpaEntity> findByNameContainingIgnoreCase(String query);


}
