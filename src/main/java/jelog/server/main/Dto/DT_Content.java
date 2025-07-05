package jelog.server.main.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Model.DN_Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Description :
 * PackageName : jelog.server.main.Dto
 * FileName : DT_Content
 * Author : User
 * TimeDate : 2023-08-03
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-08-03               User                최초생성
 * ------------------------------------------------------------
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DT_Content {
    
    private int contentId;
    
    private OsEnum contentCategories;
    
    private String contentThumbnail;
    
    @NotBlank(message = "제목은 필수입니다.")
    @Size(min = 1, max = 200, message = "제목은 1자 이상 200자 이하로 작성해주세요.")
    private String contentTitle;
    
    @NotBlank(message = "내용은 필수입니다.")
    private String contentBody;
    
    @Size(max = 500, message = "요약은 500자를 초과할 수 없습니다.")
    private String contentSummary;
    
    private int views;
    
    private int likes;
    
    @Size(max = 100, message = "작성자명은 100자를 초과할 수 없습니다.")
    private String author;
    
    private String status; // DRAFT, PUBLISHED, HIDDEN
    
    @Pattern(regexp = "^[a-z0-9-]*$", message = "Slug는 영문 소문자, 숫자, 하이픈만 사용 가능합니다.")
    private String slug;
    
    private boolean featured;
    
    private boolean allowComments;
    
    @Size(max = 160, message = "Meta description은 160자를 초과할 수 없습니다.")
    private String metaDescription;
    
    @Size(max = 255, message = "Meta keywords는 255자를 초과할 수 없습니다.")
    private String metaKeywords;
    
    private Set<String> tags;
    
    private LocalDateTime inDate;
    
    private LocalDateTime upDate;
    
    private LocalDateTime publishedDate;

    private List<DT_Files> files;

    private List<DT_Comment> comments;

    /* Table -> DTO **/
    public DT_Content(DN_Content content) {
        this.contentId = content.getContentId();
        this.contentCategories = content.getContentCategories();
        this.contentThumbnail = content.getContentThumbnail();
        this.contentTitle = content.getContentTitle();
        this.contentBody = content.getContentBody();
        this.contentSummary = content.getContentSummary();
        this.views = content.getViews();
        this.likes = content.getLikes();
        this.author = content.getAuthor();
        this.status = content.getStatus();
        this.slug = content.getSlug();
        this.featured = content.isFeatured();
        this.allowComments = content.isAllowComments();
        this.metaDescription = content.getMetaDescription();
        this.metaKeywords = content.getMetaKeywords();
        this.tags = content.getTagSet();
        this.inDate = content.getInDate();
        this.upDate = content.getUpDate();
        this.publishedDate = content.getPublishedDate();
        
        if (content.getFiles() != null) {
            this.files = content.getFiles().stream()
                    .map(DT_Files::new)
                    .collect(Collectors.toList());
        }
        
        if (content.getComments() != null) {
            this.comments = content.getComments().stream()
                    .map(DT_Comment::new)
                    .collect(Collectors.toList());
        }
    }

    // Enhanced entity conversion
    public static DN_Content toEntity(DT_Content dto) {
        DN_Content content = DN_Content.builder()
            .contentId(dto.getContentId())
            .contentCategories(dto.getContentCategories())
            .contentThumbnail(dto.getContentThumbnail())
            .contentTitle(dto.getContentTitle())
            .contentBody(dto.getContentBody())
            .contentSummary(dto.getContentSummary())
            .views(dto.getViews())
            .likes(dto.getLikes())
            .author(dto.getAuthor())
            .status(dto.getStatus() != null ? dto.getStatus() : "PUBLISHED")
            .slug(dto.getSlug())
            .featured(dto.isFeatured())
            .allowComments(dto.isAllowComments())
            .metaDescription(dto.getMetaDescription())
            .metaKeywords(dto.getMetaKeywords())
            .publishedDate(dto.getPublishedDate())
            .build();
            
        if (dto.getTags() != null) {
            content.setTagSet(dto.getTags());
        }
        
        return content;
    }
    
    // Helper method to generate slug from title
    public void generateSlugFromTitle() {
        if (this.contentTitle != null && (this.slug == null || this.slug.trim().isEmpty())) {
            this.slug = this.contentTitle
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
        }
    }
    
    // Helper method to check if content is published
    public boolean isPublished() {
        return "PUBLISHED".equals(this.status);
    }
    
    // Helper method to get comment count
    public int getCommentCount() {
        return comments != null ? comments.size() : 0;
    }
}
