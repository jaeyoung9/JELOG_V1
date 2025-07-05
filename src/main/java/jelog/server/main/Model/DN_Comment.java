package jelog.server.main.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Description :
 * PackageName : jelog.server.main.Model
 * FileName : DN_Comment
 * Author : MinJaeYoung
 * TimeDate : 2023-07-28
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-28               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "comments", indexes = {
    @Index(name = "idx_comment_content", columnList = "contentId"),
    @Index(name = "idx_comment_author", columnList = "dnUid"),
    @Index(name = "idx_comment_parent", columnList = "replyCommentId"),
    @Index(name = "idx_comment_status", columnList = "status"),
    @Index(name = "idx_comment_created", columnList = "inDate")
})
@SequenceGenerator(name = "commentSequence", sequenceName = "commentSequence", initialValue = 354985)
public class DN_Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commentSequence")
    @Column(name = "commentId")
    private int commentId;
    
    @Column(name = "contentId", nullable = false)
    private int contentId;

    @ColumnDefault("0")
    @Column(name = "replyCommentId")
    private int replyCommentId;

    @Column(name = "dnUid", length = 50)
    private String dnUid;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(min = 1, max = 2000, message = "댓글은 1자 이상 2000자 이하로 작성해주세요.")
    @Column(name = "commentBody", nullable = false, columnDefinition = "TEXT")
    private String commentBody;

    @Size(max = 100, message = "작성자명은 100자를 초과할 수 없습니다.")
    @Column(name = "commentName", length = 100)
    private String commentName;

    @Column(name = "commentPwd", length = 100)
    private String commentPwd;
    
    // New enhanced fields
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "website", length = 255)
    private String website;
    
    @Column(name = "ipAddress", length = 45)
    private String ipAddress;
    
    @Column(name = "userAgent", length = 500)
    private String userAgent;
    
    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "APPROVED"; // PENDING, APPROVED, REJECTED, SPAM
    
    @Column(name = "likes", columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private int likes = 0;
    
    @Column(name = "reported", columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private boolean reported = false;
    
    @Column(name = "moderatorNote", length = 500)
    private String moderatorNote;

    @CreationTimestamp
    @Column(name = "inDate", nullable = false, updatable = false)
    private LocalDateTime inDate;

    @UpdateTimestamp
    @Column(name = "update_date", nullable = false)
    private LocalDateTime upDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "contentId", referencedColumnName = "contentId", insertable = false, updatable = false)
    private DN_Content content;
    
    // Relationship to parent comment for nested comments
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replyCommentId", referencedColumnName = "commentId", insertable = false, updatable = false)
    private DN_Comment parentComment;
    
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DN_Comment> replies;
    
    // Helper methods
    public boolean isReply() {
        return replyCommentId > 0;
    }
    
    public boolean isApproved() {
        return "APPROVED".equals(this.status);
    }
    
    public boolean isPending() {
        return "PENDING".equals(this.status);
    }
    
    public boolean isGuest() {
        return dnUid == null || dnUid.trim().isEmpty();
    }
    
    public String getAuthorName() {
        return commentName != null && !commentName.trim().isEmpty() ? commentName : "Anonymous";
    }
    
    public int getReplyCount() {
        return replies != null ? replies.size() : 0;
    }

}
