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
import java.time.LocalDateTime;

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
@SequenceGenerator(name = "commentSequence", sequenceName = "commentSequence", initialValue = 354985)
public class DN_Comment {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commentSequence")
    private int commentId;
    private int contentId;

    @ColumnDefault("0")
    private int replyCommentId;

    private String dnUid;

    private String commentBody;

    @Nullable
    private String commentName;

    @Nullable
    private String commentPwd;

    @CreationTimestamp
    public LocalDateTime inDate;

    @UpdateTimestamp
    public LocalDateTime upDate;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "contentId", referencedColumnName = "contentId", insertable = false, updatable = false)
    private DN_Content content;

}
