package jelog.server.main.Dto;

import jelog.server.main.Model.DN_Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Description :
 * PackageName : jelog.server.main.Dto
 * FileName : DT_Comment
 * Author : User
 * TimeDate : 2023-10-25
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-10-25               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DT_Comment {
    @NotNull(message = "콘텐츠 ID는 필수입니다.")
    private int contentId;
    
    private int commentId;
    private int replyCommentId;
    private String dnUid;
    
    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(min = 1, max = 1000, message = "댓글은 1자 이상 1000자 이하로 작성해주세요.")
    private String commentBody;
    
    @NotBlank(message = "작성자 이름은 필수입니다.")
    @Size(min = 1, max = 50, message = "작성자 이름은 1자 이상 50자 이하로 작성해주세요.")
    private String commentName;
    
    @Size(min = 4, max = 20, message = "비밀번호는 4자 이상 20자 이하로 작성해주세요.")
    private String commentPwd;
    
    private DT_Content content; // Dependency reference

    public DT_Comment(DN_Comment dnComment) {
        this.contentId = dnComment.getContentId();
        this.commentId = dnComment.getCommentId();
        this.replyCommentId = dnComment.getReplyCommentId();
        this.dnUid = dnComment.getDnUid();
        this.commentBody = dnComment.getCommentBody();
        this.commentName = dnComment.getCommentName();
        this.commentPwd = dnComment.getCommentPwd();
    }
}
