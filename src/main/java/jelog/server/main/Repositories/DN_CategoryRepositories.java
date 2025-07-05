package jelog.server.main.Repositories;

import jelog.server.main.Model.DN_Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Description : Dynamic Category Repository
 * PackageName : jelog.server.main.Repositories
 * FileName : DN_CategoryRepositories
 * Author : MinJaeYoung
 * TimeDate : 2025-07-05
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2025-07-05               MinJaeYoung                     최초생성
 * ------------------------------------------------------------
 */

@Repository
public interface DN_CategoryRepositories extends JpaRepository<DN_Category, Long> {

    // Find by slug
    Optional<DN_Category> findBySlug(String slug);
    
    // Check if slug exists
    boolean existsBySlug(String slug);
    boolean existsBySlugAndIdNot(String slug, Long id);
    
    // Find by name
    Optional<DN_Category> findByName(String name);
    List<DN_Category> findByNameContainingIgnoreCase(String name);
    
    // Find by status
    List<DN_Category> findByStatus(String status);
    List<DN_Category> findByStatusOrderBySortOrderAscNameAsc(String status);
    Page<DN_Category> findByStatus(String status, Pageable pageable);
    
    // Find active categories
    @Query("SELECT c FROM DN_Category c WHERE c.status = 'ACTIVE' ORDER BY c.sortOrder ASC, c.name ASC")
    List<DN_Category> findActiveCategories();
    
    // Find featured categories
    @Query("SELECT c FROM DN_Category c WHERE c.status = 'ACTIVE' AND c.featured = true ORDER BY c.sortOrder ASC, c.name ASC")
    List<DN_Category> findFeaturedCategories();
    
    // Find categories by parent
    List<DN_Category> findByParentIdAndStatusOrderBySortOrderAscNameAsc(Long parentId, String status);
    
    // Find root categories (no parent)
    @Query("SELECT c FROM DN_Category c WHERE c.parentId IS NULL AND c.status = :status ORDER BY c.sortOrder ASC, c.name ASC")
    List<DN_Category> findRootCategories(@Param("status") String status);
    
    // Find categories with post counts
    @Query("SELECT c FROM DN_Category c WHERE c.status = 'ACTIVE' AND c.postCount > 0 ORDER BY c.postCount DESC")
    List<DN_Category> findCategoriesWithPosts();
    
    // Admin searches
    @Query("SELECT c FROM DN_Category c WHERE " +
           "(:name IS NULL OR c.name LIKE %:name%) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:parentId IS NULL OR c.parentId = :parentId)")
    Page<DN_Category> findCategoriesForAdmin(@Param("name") String name, 
                                           @Param("status") String status,
                                           @Param("parentId") Long parentId,
                                           Pageable pageable);
    
    // Get category statistics
    @Query("SELECT COUNT(c) FROM DN_Category c WHERE c.status = 'ACTIVE'")
    long countActiveCategories();
    
    @Query("SELECT SUM(c.postCount) FROM DN_Category c WHERE c.status = 'ACTIVE'")
    Long getTotalPostCount();
    
    // Find categories by creation date
    @Query("SELECT c FROM DN_Category c WHERE c.status = 'ACTIVE' ORDER BY c.createdAt DESC")
    List<DN_Category> findRecentCategories(Pageable pageable);
    
    // Search categories
    @Query("SELECT c FROM DN_Category c WHERE c.status = 'ACTIVE' AND " +
           "(c.name LIKE %:keyword% OR c.description LIKE %:keyword% OR c.metaKeywords LIKE %:keyword%)")
    List<DN_Category> searchCategories(@Param("keyword") String keyword);
    
    // Update post count
    @Query("UPDATE DN_Category c SET c.postCount = c.postCount + :increment WHERE c.id = :categoryId")
    void updatePostCount(@Param("categoryId") Long categoryId, @Param("increment") int increment);
    
    // Get category hierarchy
    @Query("SELECT c FROM DN_Category c WHERE c.status = 'ACTIVE' ORDER BY " +
           "CASE WHEN c.parentId IS NULL THEN c.sortOrder ELSE 999999 END, " +
           "c.parentId, c.sortOrder, c.name")
    List<DN_Category> getCategoryHierarchy();
}