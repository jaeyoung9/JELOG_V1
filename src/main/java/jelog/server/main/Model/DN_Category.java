package jelog.server.main.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Description : Dynamic Category Management Entity
 * PackageName : jelog.server.main.Model
 * FileName : DN_Category
 * Author : MinJaeYoung
 * TimeDate : 2025-07-05
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2025-07-05               MinJaeYoung                     최초생성
 * ------------------------------------------------------------
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "categories", indexes = {
    @Index(name = "idx_category_name", columnList = "name"),
    @Index(name = "idx_category_slug", columnList = "slug"),
    @Index(name = "idx_category_status", columnList = "status"),
    @Index(name = "idx_category_parent", columnList = "parent_id")
})
public class DN_Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "카테고리명은 필수입니다.")
    @Size(min = 1, max = 100, message = "카테고리명은 1자 이상 100자 이하로 작성해주세요.")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "카테고리 슬러그는 필수입니다.")
    @Size(min = 1, max = 100, message = "슬러그는 1자 이상 100자 이하로 작성해주세요.")
    @Column(name = "slug", nullable = false, unique = true, length = 100)
    private String slug;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "icon", length = 50)
    private String icon;

    @Column(name = "color", length = 7)
    private String color;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "sort_order", columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private Integer sortOrder = 0;

    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, DELETED

    @Column(name = "post_count", columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private Integer postCount = 0;

    @Column(name = "is_featured", columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private Boolean featured = false;

    @Column(name = "meta_title", length = 200)
    private String metaTitle;

    @Column(name = "meta_description", length = 500)
    private String metaDescription;

    @Column(name = "meta_keywords", length = 500)
    private String metaKeywords;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    // Helper methods
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    public boolean isInactive() {
        return "INACTIVE".equals(this.status);
    }

    public boolean isDeleted() {
        return "DELETED".equals(this.status);
    }

    public boolean hasParent() {
        return this.parentId != null;
    }

    public void incrementPostCount() {
        this.postCount = (this.postCount == null ? 0 : this.postCount) + 1;
    }

    public void decrementPostCount() {
        this.postCount = Math.max(0, (this.postCount == null ? 0 : this.postCount) - 1);
    }
}