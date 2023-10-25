package jelog.server.main.Dto;

import jelog.server.main.Model.DN_Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private int commentId;
    private int replyCommentId;
    private String dnUid;
    private String commentBody;
    private String commentName;
    private String commentPwd;
    private DT_Content content; // Dependency reference

    public DT_Comment(DN_Comment dnComment) {
        this.commentId = dnComment.getCommentId();
        this.replyCommentId = dnComment.getReplyCommentId();
        this.dnUid = dnComment.getDnUid();
        this.commentBody = dnComment.getCommentBody();
        this.commentName = dnComment.getCommentName();
        this.commentPwd = dnComment.getCommentPwd();
    }
}
