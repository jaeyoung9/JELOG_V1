package jelog.server.main.Controller.SNS1011;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Dto.DT_Comment;
import jelog.server.main.Dto.DT_Content;
import jelog.server.main.Dto.DT_Files;
import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Global.ResponseDTO;
import jelog.server.main.Model.DN_Comment;
import jelog.server.main.Model.DN_Content;
import jelog.server.main.Service.DN_CommentService;
import jelog.server.main.Service.DN_ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Description :
 * PackageName : jelog.server.main.Controller.SNS1011
 * FileName : SNS1011Controller
 * Author : MinJaeYoung
 * TimeDate : 2023-07-28
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-28               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 * 2023-08-02               MinJaeYoung                내용추가
 * ------------------------------------------------------------
 * 2023-09-28               MinJaeYoung                상세페이지 테스트
 */

@RestController
@RequestMapping(value = "/api/public")
public class SNS1011Controller extends BaseController {

    /**
     * [Variables]
     * DN_ContentService
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    private DN_ContentService contentService;
    private DN_CommentService commentService;
    @Autowired
    public SNS1011Controller(DN_ContentService _contentService, DN_CommentService _commentService){
        this.contentService = _contentService;
        this.commentService = _commentService;
    }


    /**
     * [Main]
     * Main Page Result Data
     * */
    @GetMapping(value = "/mains/")
    public ResponseEntity<?> mains(@PageableDefault(size = 10) Pageable pageable, String Title, OsEnum Categories){

        Map<String, Object> map = new HashMap<>();
        map.put("data",contentService.findPage(pageable, Title, Categories));
        ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
        return ResponseEntity.ok().body(responseDTO);
    }

    /**
     * [Main]
     * Main Detail Page
     * 1. 페이지 접근시 조회수 카운팅 필요. Ok.
     * 2. 작성일 자동등록 안되는 내용 확인 필요. Ok
     * 2-1. 현재 DN_Content 모델에 등록된 어노텐션 @CreationTimestamp 관련 내용 수집필요.
     * */
    @GetMapping(value = "/mains/relay/{listNumber}/")
    public ResponseEntity<?> mainPage(@PathVariable int listNumber, HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException {

        // page In User Cookie
        Cookie[] cookies = request.getCookies();
        Map<String, Cookie> cookieMap = new HashMap<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        Cookie viewedCookie = cookieMap.get(hashCookieName(String.valueOf(listNumber)));

        // get data through number
        DN_Content getContent = contentService.findContent(listNumber);

        if(viewedCookie == null){
            contentService.contentViewCount(listNumber);
            Cookie cookie =  new Cookie(hashCookieName(String.valueOf(listNumber)), "1");
            cookie.setMaxAge(60*60*24);
            cookie.setPath("/");
            response.addCookie(cookie);
        }



        Map<String, Object> map = new HashMap<>();
        map.put("data", getContent);
        ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
        return ResponseEntity.ok().body(responseDTO);
    }

    /**
     * [Detail]
     * Detail Comment
     * 1. 상세 페이지 댓글 등록
     * */
    @PostMapping("/mains/relay/{listNumber}/comments/")
    public ResponseEntity<?> storeComment(@PathVariable int listNumber, @RequestBody DT_Comment dto){
        try{
            DN_Comment checkComment = commentService.createComment(dto);
            Map<String, Object> map = new HashMap<>();
            map.put("data", checkComment);
            ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            return null;
        }
    }
}
