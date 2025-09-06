package com.clucid.server.terms.persist.jpa;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.Type;

import com.vladmihalcea.hibernate.type.json.JsonType;

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

	@Type(JsonType.class)  // 하이버네이트 6 (hibernate-types 라이브러리 필요)
	@Column(columnDefinition = "jsonb", nullable = false)
	@Builder.Default
	private List<String> definitions = List.of();

	@Type(JsonType.class)  // 하이버네이트 6 (hibernate-types 라이브러리 필요)
	@Column(columnDefinition = "jsonb", nullable = false)
	@Builder.Default
	private List<String> examples = List.of();
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
