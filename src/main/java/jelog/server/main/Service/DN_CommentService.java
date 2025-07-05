package jelog.server.main.Service;

import jelog.server.main.Dto.DT_Comment;
import jelog.server.main.Global.Encrypt;
import jelog.server.main.Model.DN_Comment;
import jelog.server.main.Repositories.DN_CommentRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * Description :
 * PackageName : jelog.server.main.Service
 * FileName : DN_CommentService
 * Author : MinJaeYoung
 * TimeDate : 2023-07-31
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-31               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 * 2024-04-08               jyMin                @Autowired 수정
 */

@Slf4j
@Service
public class DN_CommentService {

    /**
     * [Variables]
     * Repository
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    private final DN_CommentRepositories commentRepositories;

    @Autowired
    private DN_CommentService(DN_CommentRepositories commentRepositories){
        this.commentRepositories = commentRepositories;
    }
    /**
     * [% User Name]
     *
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    private static final Random random = new Random();
    private static final String CHOSUNG = "ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ";
    private static final String JUNGSUNG = "ㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟㅠㅡㅢㅣ";
    private static final String JONGSUNG = " ㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎ";

    public static String generateName() {
        StringBuilder name = new StringBuilder("익명의 ");

        try {
            int nameLength = random.nextInt(2) + 2; // 2~3글자 이름 생성

            for (int i = 0; i < nameLength; i++) {
                char cho = CHOSUNG.charAt(random.nextInt(CHOSUNG.length()));
                char jung = JUNGSUNG.charAt(random.nextInt(JUNGSUNG.length()));
                char jong = JONGSUNG.charAt(random.nextInt(JONGSUNG.length()));

                int choIndex = CHOSUNG.indexOf(cho);
                int jungIndex = JUNGSUNG.indexOf(jung);
                int jongIndex = (jong == ' ') ? 0 : JONGSUNG.indexOf(jong) + 1;

                int unicode = 0xAC00 + (choIndex * 21 * 28) + (jungIndex * 28) + jongIndex;
                name.append((char) unicode);
            }
        }catch (Exception e) {
            System.err.println("Error generating name: " + e.getMessage());
            e.printStackTrace();
        }

        return name.toString();
    }
    //-------------------------------------------------------------------------------------------------------------------------------------

    /**
     * [Comment]
     * Validate
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    public void validateComment(final DT_Comment _model){
        if(null == _model){
            log.warn("data is check for cannot be null.");
            throw new RuntimeException("data is check for cannot be null.");
        }
    }


    /**
     * [Comment] Create Comment
     * @param entity 게시글 댓글 데이터
     * @return DN_Content content 데이터
     * */
    public DN_Comment createComment(DT_Comment entity){

        validateComment(entity);

        DN_Comment comment = new DN_Comment();
        comment.setContentId(entity.getContentId());
        comment.setCommentName(generateName());
        comment.setCommentPwd(Encrypt.getSalt());
        comment.setCommentBody(entity.getCommentBody());
        comment = commentRepositories.save(comment);
        return comment;
    }
    
    /**
     * Get total comment count
     */
    public long getTotalCommentCount() {
        return commentRepositories.count();
    }
}
