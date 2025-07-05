package jelog.server.main.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Enum.OsEnumConverter;
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
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Description :
 * PackageName : jelog.server.main.Model
 * FileName : DN_Content
 * Author : MinJaeYoung
 * TimeDate : 2023-07-28
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-28               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 *     contentCategories Enum
 *      - Other("Other_Categories", 49847),
 *      - Java("Java_Categories",42958),
 *      - JavaScript("JavaScript_Categories",42959),
 *      - C("C_Categories",47685),
 *      - Python("Python",45656),
 *      - Shell("ShellScript_Categories",  48765),
 *      - Security("Security_Categories", 41232),
 *      - DeveloperCareerSkills("DeveloperCareerSkills_Categories",49999),
 *
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "content", indexes = {
    @Index(name = "idx_content_title", columnList = "contentTitle"),
    @Index(name = "idx_content_category", columnList = "contentCategories"),
    @Index(name = "idx_content_author", columnList = "author"),
    @Index(name = "idx_content_status", columnList = "status"),
    @Index(name = "idx_content_created", columnList = "inDate")
})
@SequenceGenerator(name = "contentSequence", sequenceName = "contentSequence", initialValue = 987685)
public class DN_Content {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contentSequence")
    private int contentId;

    @Convert(converter = OsEnumConverter.class)
    @Column(name = "contentCategories")
    private OsEnum contentCategories;

    @Column(name = "contentThumbnail")
    private String contentThumbnail;
    
    @NotBlank(message = "제목은 필수입니다.")
    @Size(min = 1, max = 200, message = "제목은 1자 이상 200자 이하로 작성해주세요.")
    @Column(name = "contentTitle", nullable = false, length = 200)
    private String contentTitle;

    @NotBlank(message = "내용은 필수입니다.")
    @Lob
    @Column(name = "contentBody", columnDefinition = "LONGTEXT")
    private String contentBody;
    
    @Column(name = "contentSummary", length = 500)
    private String contentSummary;
    
    @Column(name = "views", columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private int views = 0;
    
    @Column(name = "likes", columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private int likes = 0;
    
    @Size(max = 100, message = "작성자명은 100자를 초과할 수 없습니다.")
    @Column(name = "author", length = 100)
    private String author;
    
    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "PUBLISHED"; // DRAFT, PUBLISHED, HIDDEN, DELETED
    
    @Column(name = "slug", unique = true, length = 200)
    private String slug; // URL-friendly identifier
    
    @Column(name = "featured", columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private boolean featured = false;
    
    @Column(name = "allowComments", columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Builder.Default
    private boolean allowComments = true;
    
    @Column(name = "metaDescription", length = 160)
    private String metaDescription;
    
    @Column(name = "metaKeywords", length = 255)
    private String metaKeywords;

    // Tags as comma-separated string for simplicity
    @Column(name = "tags", length = 500)
    private String tags;
    
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DN_Files> files;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DN_Comment> comments;

    @CreationTimestamp
    @Column(name = "inDate", nullable = false, updatable = false)
    private LocalDateTime inDate;

    @UpdateTimestamp
    @Column(name = "update_date", nullable = false)
    private LocalDateTime upDate;
    
    @Column(name = "publishedDate")
    private LocalDateTime publishedDate;
    
    // Helper methods
    public Set<String> getTagSet() {
        Set<String> tagSet = new HashSet<>();
        if (tags != null && !tags.trim().isEmpty()) {
            String[] tagArray = tags.split(",");
            for (String tag : tagArray) {
                tagSet.add(tag.trim());
            }
        }
        return tagSet;
    }
    
    public void setTagSet(Set<String> tagSet) {
        if (tagSet != null && !tagSet.isEmpty()) {
            this.tags = String.join(",", tagSet);
        } else {
            this.tags = null;
        }
    }
    
    public boolean isPublished() {
        return "PUBLISHED".equals(this.status);
    }
    
    public boolean isDraft() {
        return "DRAFT".equals(this.status);
    }
    
    public int getCommentCount() {
        return comments != null ? comments.size() : 0;
    }

}
