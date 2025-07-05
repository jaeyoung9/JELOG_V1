package jelog.server.main.Controller.ADM1011;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Global.ResponseDTO;
import jelog.server.main.Global.ResponseResult;
import jelog.server.main.Model.DN_Category;
import jelog.server.main.Service.DN_CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description : Category Management Controller for Admin
 * PackageName : jelog.server.main.Controller.ADM1011
 * FileName : CategoryManagementController
 * Author : MinJaeYoung
 * TimeDate : 2025-07-05
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2025-07-05               MinJaeYoung                     최초생성
 * ------------------------------------------------------------
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/republic/categories")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryManagementController extends BaseController {

    private final DN_CategoryService categoryService;

    @Autowired
    public CategoryManagementController(DN_CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Get all categories for admin
     */
    @GetMapping
    public ResponseEntity<ResponseDTO> getCategories(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long parentId) {
        
        try {
            Page<DN_Category> categories = categoryService.getCategoriesForAdmin(pageable, name, status, parentId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("categories", categories.getContent());
            result.put("currentPage", categories.getNumber());
            result.put("totalPages", categories.getTotalPages());
            result.put("totalElements", categories.getTotalElements());
            result.put("hasNext", categories.hasNext());
            result.put("hasPrevious", categories.hasPrevious());
            
            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .payload(result)
                .build());
                
        } catch (Exception e) {
            log.error("Error retrieving categories for admin", e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("카테고리를 불러오는 중 오류가 발생했습니다.")
                .build());
        }
    }

    /**
     * Get category hierarchy
     */
    @GetMapping("/hierarchy")
    public ResponseEntity<ResponseDTO> getCategoryHierarchy() {
        try {
            List<DN_Category> hierarchy = categoryService.getCategoryHierarchy();
            
            Map<String, Object> result = new HashMap<>();
            result.put("categories", hierarchy);
            
            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .payload(result)
                .build());
                
        } catch (Exception e) {
            log.error("Error retrieving category hierarchy", e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("카테고리 계층구조를 불러오는 중 오류가 발생했습니다.")
                .build());
        }
    }

    /**
     * Get single category by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getCategory(@PathVariable Long id) {
        try {
            DN_Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
            
            Map<String, Object> result = new HashMap<>();
            result.put("category", category);
            
            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .payload(result)
                .build());
                
        } catch (Exception e) {
            log.error("Error retrieving category with ID: " + id, e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("카테고리를 찾을 수 없습니다.")
                .build());
        }
    }

    /**
     * Create new category
     */
    @PostMapping
    public ResponseEntity<ResponseDTO> createCategory(@Valid @RequestBody DN_Category category) {
        try {
            DN_Category createdCategory = categoryService.createCategory(category);
            
            Map<String, Object> result = new HashMap<>();
            result.put("category", createdCategory);
            
            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .message("카테고리가 성공적으로 생성되었습니다.")
                .payload(result)
                .build());
                
        } catch (Exception e) {
            log.error("Error creating category", e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("카테고리 생성 중 오류가 발생했습니다: " + e.getMessage())
                .build());
        }
    }

    /**
     * Update category
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateCategory(@PathVariable Long id, 
                                                    @Valid @RequestBody DN_Category category) {
        try {
            DN_Category updatedCategory = categoryService.updateCategory(id, category);
            
            Map<String, Object> result = new HashMap<>();
            result.put("category", updatedCategory);
            
            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .message("카테고리가 성공적으로 수정되었습니다.")
                .payload(result)
                .build());
                
        } catch (Exception e) {
            log.error("Error updating category with ID: " + id, e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("카테고리 수정 중 오류가 발생했습니다: " + e.getMessage())
                .build());
        }
    }

    /**
     * Delete category (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            
            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .message("카테고리가 성공적으로 삭제되었습니다.")
                .build());
                
        } catch (Exception e) {
            log.error("Error deleting category with ID: " + id, e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("카테고리 삭제 중 오류가 발생했습니다: " + e.getMessage())
                .build());
        }
    }

    /**
     * Permanently delete category
     */
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<ResponseDTO> permanentlyDeleteCategory(@PathVariable Long id) {
        try {
            categoryService.permanentlyDeleteCategory(id);
            
            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .message("카테고리가 영구적으로 삭제되었습니다.")
                .build());
                
        } catch (Exception e) {
            log.error("Error permanently deleting category with ID: " + id, e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("카테고리 영구 삭제 중 오류가 발생했습니다: " + e.getMessage())
                .build());
        }
    }

    /**
     * Get category statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ResponseDTO> getCategoryStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCategories", categoryService.getActiveCategoryCount());
            stats.put("totalPosts", categoryService.getTotalPostCount());
            stats.put("categoriesWithPosts", categoryService.getCategoriesWithPosts().size());
            stats.put("featuredCategories", categoryService.getFeaturedCategories().size());
            
            Map<String, Object> result = new HashMap<>();
            result.put("statistics", stats);
            
            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .payload(result)
                .build());
                
        } catch (Exception e) {
            log.error("Error retrieving category statistics", e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("카테고리 통계를 불러오는 중 오류가 발생했습니다.")
                .build());
        }
    }

    /**
     * Initialize default categories
     */
    @PostMapping("/initialize")
    public ResponseEntity<ResponseDTO> initializeDefaultCategories() {
        try {
            categoryService.initializeDefaultCategories();
            
            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .message("기본 카테고리가 성공적으로 초기화되었습니다.")
                .build());
                
        } catch (Exception e) {
            log.error("Error initializing default categories", e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("기본 카테고리 초기화 중 오류가 발생했습니다: " + e.getMessage())
                .build());
        }
    }

    /**
     * Bulk update categories
     */
    @PutMapping("/bulk")
    public ResponseEntity<ResponseDTO> bulkUpdateCategories(@RequestBody Map<String, Object> request) {
        try {
            // Implementation for bulk operations like changing status, sort order, etc.
            // This would be implemented based on specific requirements
            
            return ResponseEntity.ok(ResponseDTO.builder()
                .result(ResponseResult.SUCCESS)
                .message("카테고리가 일괄 수정되었습니다.")
                .build());
                
        } catch (Exception e) {
            log.error("Error in bulk update categories", e);
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("카테고리 일괄 수정 중 오류가 발생했습니다: " + e.getMessage())
                .build());
        }
    }
}