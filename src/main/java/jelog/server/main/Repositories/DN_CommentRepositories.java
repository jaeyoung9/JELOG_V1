package jelog.server.main.Repositories;

import jelog.server.main.Model.DN_Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Description :
 * PackageName : jelog.server.main.Repositories
 * FileName : DN_CommentRepositories
 * Author : MinJaeYoung
 * TimeDate : 2023-07-31
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-31               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@Repository
public interface DN_CommentRepositories extends JpaRepository<DN_Comment, Integer> {

    // 콘텐츠 Id로 댓글 불러오기 (단일)
    Optional<DN_Comment> findByContentId(Integer contentId);

    // 콘텐츠 Id로 댓글 전체 불러오기
    Optional<List<DN_Comment>> findAllByCommentId(Integer contentId);

    // 대댓글 확인하기
    Optional<DN_Comment> findByReplyCommentId(Integer commentId);

    // 콘텐츠 Id에 댓글 카운트
    Long countByContentId(Integer contentId);

}
