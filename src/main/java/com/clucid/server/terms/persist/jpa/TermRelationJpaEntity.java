package com.clucid.server.terms.persist.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "term_relations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermRelationJpaEntity {

	@Id
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "term_id")
	private TermJpaEntity term;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "related_term_id")
	private TermJpaEntity relatedTerm;

	@PrePersist
	public void onPrePersist() {
		if (this.id == null) {
			this.id = java.util.UUID.randomUUID().toString();
		}
	}
}
