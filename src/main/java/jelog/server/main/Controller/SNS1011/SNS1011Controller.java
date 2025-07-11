package jelog.server.main.Controller.SNS1011;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Dto.DT_Comment;
import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Global.ResponseDTO;
import jelog.server.main.Model.DN_Comment;
import jelog.server.main.Model.DN_Content;
import jelog.server.main.Service.DN_CommentService;
import jelog.server.main.Service.DN_ContentService;
import jelog.server.main.Service.DN_CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

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

    private static final Logger log = LoggerFactory.getLogger(SNS1011Controller.class);

    /**
     * [Variables]
     * DN_ContentService
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    private final DN_ContentService contentService;
    private final DN_CommentService commentService;
    private final DN_CategoryService categoryService;
    @Autowired
    public SNS1011Controller(DN_ContentService _contentService, DN_CommentService _commentService, DN_CategoryService _categoryService){
        this.contentService = _contentService;
        this.commentService = _commentService;
        this.categoryService = _categoryService;
    }


    /**
     * [Main]
     * Main Page Result Data
     * */
    @GetMapping(value = "/mains/")
    public ResponseEntity<?> mains(@PageableDefault(size = 10) Pageable pageable, 
                                  @RequestParam(required = false) String Title, 
                                  @RequestParam(required = false) String Categories,
                                  @RequestParam(required = false) String sort){

        Map<String, Object> map = new HashMap<>();
        
        try {
            // Convert Categories string to OsEnum using the fromValue method
            OsEnum categoryEnum = null;
            if (Categories != null && !Categories.trim().isEmpty()) {
                categoryEnum = OsEnum.fromValue(Categories.trim());
            }
            
            // Handle sorting - convert "latest" to actual field name
            if ("latest".equals(sort)) {
                // Create new pageable with proper sorting by publishedDate descending
                pageable = org.springframework.data.domain.PageRequest.of(
                    pageable.getPageNumber(), 
                    pageable.getPageSize(), 
                    org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "publishedDate")
                );
            } else if ("views".equals(sort)) {
                // Sort by views descending
                pageable = org.springframework.data.domain.PageRequest.of(
                    pageable.getPageNumber(), 
                    pageable.getPageSize(), 
                    org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "views")
                );
            } else if ("comments".equals(sort)) {
                // Sort by comment count (this would require a custom query, for now use views)
                pageable = org.springframework.data.domain.PageRequest.of(
                    pageable.getPageNumber(), 
                    pageable.getPageSize(), 
                    org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "views")
                );
            }
            
            map.put("data",contentService.findPage(pageable, Title, categoryEnum));
            ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (IllegalArgumentException e) {
            log.error("Invalid category parameter: " + Categories, e);
            // If category is invalid, return all content without category filter
            map.put("data",contentService.findPage(pageable, Title, null));
            ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.error("Error loading main content", e);
            ResponseDTO responseDTO = ResponseDTO.builder()
                .result(jelog.server.main.Global.ResponseResult.FAIL)
                .message("콘텐츠를 불러오는데 실패했습니다.")
                .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * [Main]
     * Featured Content
     * */
    @GetMapping(value = "/mains/featured")
    public ResponseEntity<?> featuredContent(@RequestParam(defaultValue = "3") int size){
        Map<String, Object> map = new HashMap<>();
        
        // Create pageable for featured content
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
            0, size, 
            org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "views", "inDate")
        );
        
        map.put("data", contentService.findPage(pageable, null, null));
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
    public ResponseEntity<?> storeComment(@PathVariable int listNumber, @RequestBody @Valid DT_Comment dto){
        try{
            dto.setCommentBody(encodeForHtml(dto.getCommentBody()));
            DN_Comment checkComment = commentService.createComment(dto);
            Map<String, Object> map = new HashMap<>();
            map.put("data", checkComment);
            ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.error("Error processing comment submission", e);
            ResponseDTO responseDTO = ResponseDTO.builder()
                .result(jelog.server.main.Global.ResponseResult.FAIL)
                .message("댓글 등록 중 오류가 발생했습니다.")
                .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * [Statistics]
     * Get blog statistics
     */
    @GetMapping(value = "/stats")
    public ResponseEntity<?> getStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Get total published posts count
            long totalPosts = contentService.getTotalPostCount();
            
            // Get total views (sum of all post views)
            long totalViews = contentService.getTotalViews();
            
            // Get dynamic category count
            long totalCategories = categoryService.getActiveCategoryCount();
            
            stats.put("totalPosts", totalPosts);
            stats.put("totalViews", totalViews);
            stats.put("totalCategories", totalCategories);
            
            Map<String, Object> map = new HashMap<>();
            map.put("data", stats);
            
            ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.error("Error loading statistics", e);
            ResponseDTO responseDTO = ResponseDTO.builder()
                .result(jelog.server.main.Global.ResponseResult.FAIL)
                .message("통계를 불러오는데 실패했습니다.")
                .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * [Like]
     * Toggle like for a post
     */
    @PostMapping(value = "/posts/{postId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable int postId, HttpServletRequest request) {
        try {
            // Check if the content exists and is published
            DN_Content content = contentService.findPublishedPost(postId);
            if (content == null) {
                ResponseDTO responseDTO = ResponseDTO.builder()
                    .result(jelog.server.main.Global.ResponseResult.FAIL)
                    .message("게시글을 찾을 수 없습니다.")
                    .build();
                return ResponseEntity.notFound().build();
            }
            
            // For now, just increment the like count (in a real app, you'd track user likes)
            content.setLikes(content.getLikes() + 1);
            
            // Note: In a production app, you would:
            // 1. Check if user already liked this post
            // 2. Store like/unlike status per user
            // 3. Prevent multiple likes from same user
            
            Map<String, Object> map = new HashMap<>();
            map.put("liked", true);
            map.put("likeCount", content.getLikes());
            
            ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
            return ResponseEntity.ok().body(responseDTO);
            
        } catch (Exception e) {
            log.error("Error toggling like for post: " + postId, e);
            ResponseDTO responseDTO = ResponseDTO.builder()
                .result(jelog.server.main.Global.ResponseResult.FAIL)
                .message("좋아요 처리 중 오류가 발생했습니다.")
                .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * [Categories]
     * Get all active categories
     */
    @GetMapping(value = "/categories")
    public ResponseEntity<?> getCategories() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("data", categoryService.getAllActiveCategories());
            
            ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.error("Error loading categories", e);
            ResponseDTO responseDTO = ResponseDTO.builder()
                .result(jelog.server.main.Global.ResponseResult.FAIL)
                .message("카테고리를 불러오는데 실패했습니다.")
                .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * [Categories]
     * Get featured categories
     */
    @GetMapping(value = "/categories/featured")
    public ResponseEntity<?> getFeaturedCategories() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("data", categoryService.getFeaturedCategories());
            
            ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.error("Error loading featured categories", e);
            ResponseDTO responseDTO = ResponseDTO.builder()
                .result(jelog.server.main.Global.ResponseResult.FAIL)
                .message("추천 카테고리를 불러오는데 실패했습니다.")
                .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
