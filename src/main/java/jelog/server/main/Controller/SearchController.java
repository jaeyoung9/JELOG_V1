package jelog.server.main.Controller;

import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Global.ResponseDTO;
import jelog.server.main.Global.ResponseResult;
import jelog.server.main.Model.DN_Content;
import jelog.server.main.Service.DN_ContentService;
import jelog.server.main.Service.DN_LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description : 검색 기능 컨트롤러
 * PackageName : jelog.server.main.Controller
 * FileName : SearchController
 * Author : MinJaeYoung
 * TimeDate : 2024-01-01
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2024-01-01               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);
    
    private final DN_ContentService contentService;
    private final DN_LogService logService;

    @Autowired
    public SearchController(DN_ContentService contentService, DN_LogService logService) {
        this.contentService = contentService;
        this.logService = logService;
    }

    /**
     * 통합 검색 - 제목, 내용, 태그로 검색
     */
    @GetMapping("/posts")
    public ResponseEntity<ResponseDTO> searchPosts(
            @RequestParam String keyword,
            @RequestParam(required = false) OsEnum category,
            @RequestParam(required = false) String sortBy,
            @PageableDefault(size = 10, sort = "contentId") Pageable pageable) {
        
        try {
            // 키워드 유효성 검증
            if (keyword == null || keyword.trim().length() < 2) {
                return ResponseEntity.badRequest().body(ResponseDTO.builder()
                    .message("검색 키워드는 최소 2자 이상이어야 합니다.")
                    .build());
            }

            // 검색 키워드 로깅 (통계용)
            logService.logSearchKeyword(keyword.trim());

            // 검색 실행
            Page<DN_Content> searchResults = contentService.searchPosts(
                pageable, keyword.trim(), category, sortBy);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("posts", searchResults.getContent());
            responseData.put("currentPage", searchResults.getNumber());
            responseData.put("totalPages", searchResults.getTotalPages());
            responseData.put("totalElements", searchResults.getTotalElements());
            responseData.put("hasNext", searchResults.hasNext());
            responseData.put("hasPrevious", searchResults.hasPrevious());
            responseData.put("searchKeyword", keyword.trim());

            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .payload(responseData)
                .build());

        } catch (Exception e) {
            log.error("Error searching posts with keyword: " + keyword, e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("검색 중 오류가 발생했습니다.")
                .build());
        }
    }

    /**
     * 카테고리별 게시물 조회
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<ResponseDTO> getPostsByCategory(
            @PathVariable OsEnum category,
            @PageableDefault(size = 10, sort = "contentId") Pageable pageable) {
        
        try {
            Page<DN_Content> posts = contentService.findByCategory(pageable, category);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("posts", posts.getContent());
            responseData.put("currentPage", posts.getNumber());
            responseData.put("totalPages", posts.getTotalPages());
            responseData.put("totalElements", posts.getTotalElements());
            responseData.put("hasNext", posts.hasNext());
            responseData.put("hasPrevious", posts.hasPrevious());
            responseData.put("category", category);

            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .payload(responseData)
                .build());

        } catch (Exception e) {
            log.error("Error retrieving posts by category: " + category, e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("카테고리별 게시물 조회 중 오류가 발생했습니다.")
                .build());
        }
    }

    /**
     * 태그 검색
     */
    @GetMapping("/tags")
    public ResponseEntity<ResponseDTO> searchByTag(
            @RequestParam String tag,
            @PageableDefault(size = 10, sort = "contentId") Pageable pageable) {
        
        try {
            // 태그 유효성 검증
            if (tag == null || tag.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ResponseDTO.builder()
                    .result(ResponseResult.FAIL)
                    .message("태그가 입력되지 않았습니다.")
                    .build());
            }

            Page<DN_Content> posts = contentService.findByTag(pageable, tag.trim());

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("posts", posts.getContent());
            responseData.put("currentPage", posts.getNumber());
            responseData.put("totalPages", posts.getTotalPages());
            responseData.put("totalElements", posts.getTotalElements());
            responseData.put("hasNext", posts.hasNext());
            responseData.put("hasPrevious", posts.hasPrevious());
            responseData.put("tag", tag.trim());

            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .payload(responseData)
                .build());

        } catch (Exception e) {
            log.error("Error searching posts by tag: " + tag, e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("태그 검색 중 오류가 발생했습니다.")
                .build());
        }
    }

    /**
     * 인기 검색어 조회
     */
    @GetMapping("/popular-keywords")
    public ResponseEntity<ResponseDTO> getPopularKeywords(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<String> popularKeywords = logService.getPopularSearchKeywords(limit);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("keywords", popularKeywords);

            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .payload(responseData)
                .build());

        } catch (Exception e) {
            log.error("Error retrieving popular keywords", e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("인기 검색어 조회 중 오류가 발생했습니다.")
                .build());
        }
    }

    /**
     * 검색 제안 (자동완성)
     */
    @GetMapping("/suggestions")
    public ResponseEntity<ResponseDTO> getSearchSuggestions(@RequestParam String query) {
        try {
            // 최소 길이 검증
            if (query == null || query.trim().length() < 2) {
                return ResponseEntity.ok(ResponseDTO.builder()
                    .result(ResponseResult.SUCCESS)
                    .payload(Map.of("suggestions", List.of()))
                    .build());
            }

            List<String> suggestions = contentService.getSearchSuggestions(query.trim());

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("suggestions", suggestions);

            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .payload(responseData)
                .build());

        } catch (Exception e) {
            log.error("Error retrieving search suggestions for: " + query, e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("검색 제안 조회 중 오류가 발생했습니다.")
                .build());
        }
    }

    /**
     * 관련 게시물 조회
     */
    @GetMapping("/related/{postId}")
    public ResponseEntity<ResponseDTO> getRelatedPosts(@PathVariable int postId, @RequestParam(defaultValue = "5") int limit) {
        try {
            List<DN_Content> relatedPosts = contentService.findRelatedPosts(postId, limit);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("relatedPosts", relatedPosts);

            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .payload(responseData)
                .build());

        } catch (Exception e) {
            log.error("Error retrieving related posts for postId: " + postId, e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("관련 게시물 조회 중 오류가 발생했습니다.")
                .build());
        }
    }

    /**
     * 고급 검색
     */
    @PostMapping("/advanced")
    public ResponseEntity<ResponseDTO> advancedSearch(@RequestBody Map<String, Object> searchCriteria,
                                                    @PageableDefault(size = 10) Pageable pageable) {
        try {
            // 검색 조건 추출
            String keyword = (String) searchCriteria.get("keyword");
            String category = (String) searchCriteria.get("category");
            String dateFrom = (String) searchCriteria.get("dateFrom");
            String dateTo = (String) searchCriteria.get("dateTo");
            String author = (String) searchCriteria.get("author");

            Page<DN_Content> searchResults = contentService.advancedSearch(
                pageable, keyword, category, dateFrom, dateTo, author);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("posts", searchResults.getContent());
            responseData.put("currentPage", searchResults.getNumber());
            responseData.put("totalPages", searchResults.getTotalPages());
            responseData.put("totalElements", searchResults.getTotalElements());
            responseData.put("hasNext", searchResults.hasNext());
            responseData.put("hasPrevious", searchResults.hasPrevious());
            responseData.put("searchCriteria", searchCriteria);

            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .payload(responseData)
                .build());

        } catch (Exception e) {
            log.error("Error in advanced search", e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("고급 검색 중 오류가 발생했습니다.")
                .build());
        }
    }
}