package com.clucid.server.terms.persist.jpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagJpaEntity {
	@Id
	private String id;

	@Column(nullable = false, length = 100, unique = true)
	private String name;

	@Column(name = "ref_count", nullable = false)
	@Builder.Default
	private int refCount = 0;


	@PrePersist
	public void onPrePersist() {
		if (this.id == null) {
			this.id = java.util.UUID.randomUUID().toString();
		}
	}

	public void incrementRefCount() {
		this.refCount++;
	}
}
