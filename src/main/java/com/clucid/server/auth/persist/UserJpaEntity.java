package com.clucid.server.auth.persist;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.Type;

import com.vladmihalcea.hibernate.type.json.JsonType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJpaEntity {

	@Id
	private String id;   // UUID 문자열 직접 생성해서 set

	@Column(nullable = false, unique = true, length = 255)
	private String email;

	@Column(nullable = false, length = 255)
	private String password;

	@Column(nullable = false, length = 50)
	private String nickname;

	@Column(nullable = false, length = 20)
	private String role;  // e.g., "USER", "ADMIN"

	@Column(name = "profile_image_url", columnDefinition = "TEXT")
	private String profileImageUrl;

	@Type(JsonType.class)  // 하이버네이트 6 (hibernate-types 라이브러리 필요)
	@Column(columnDefinition = "jsonb", nullable = false, name = "recent_searches")
	@Builder.Default
	private List<String> recentSearches = List.of();

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
