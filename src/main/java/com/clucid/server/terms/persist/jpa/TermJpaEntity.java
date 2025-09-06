package com.clucid.server.terms.persist.jpa;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "terms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermJpaEntity {

	@Id
	private String id; // UUID

	@Column(name = "term_korean", nullable = false, length = 255)
	private String termKorean;

	@Column(name = "term_english", length = 255)
	private String termEnglish;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String definition;
	@Column(name ="img_url", columnDefinition = "TEXT")
	private String imgUrl;

	@Column(name = "created_at", updatable = false,
		columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdAt;

	@Column(name = "updated_at",
		columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime updatedAt;

	@PrePersist
	public void onPrePersist() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		if (this.id == null) {
			this.id = java.util.UUID.randomUUID().toString();
		}
	}

	@PreUpdate
	public void onPreUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
