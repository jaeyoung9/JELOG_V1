package jelog.server.main.Repositories;

import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Model.DN_Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Description :
 * PackageName : jelog.server.main.Repositories
 * FileName : DN_ContentRepositories
 * Author : MinJaeYoung
 * TimeDate : 2023-07-31
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-31               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@Repository
public interface DN_ContentRepositories extends JpaRepository<DN_Content, Integer> {

    // Existing methods
    DN_Content findByContentId(Integer contentId);
    Optional<List<DN_Content>> findAllBy();
    Page<DN_Content> findDN_ContentByContentTitleContainsAndContentCategoriesInOrContentCategoriesIsNullOrderByContentIdDesc(String contentTitle, Set<OsEnum> categories, Pageable pageable);

    // Enhanced methods for the new features
    
    // Find by status
    Page<DN_Content> findByStatus(String status, Pageable pageable);
    Page<DN_Content> findByStatusOrderByPublishedDateDesc(String status, Pageable pageable);
    
    // Find by status and ID
    Optional<DN_Content> findByContentIdAndStatus(int contentId, String status);
    
    // Search methods
    Page<DN_Content> findByStatusAndContentTitleContainingIgnoreCase(String status, String title, Pageable pageable);
    Page<DN_Content> findByStatusAndContentTitleContainingIgnoreCaseOrContentBodyContainingIgnoreCase(
        String status, String title, String body, Pageable pageable);
    
    // Category-based searches
    Page<DN_Content> findByStatusAndContentCategories(String status, OsEnum category, Pageable pageable);
    Page<DN_Content> findByStatusAndContentTitleContainingIgnoreCaseAndContentCategories(
        String status, String title, OsEnum category, Pageable pageable);
    
    // Author-based searches
    Page<DN_Content> findByAuthorOrderByInDateDesc(String author, Pageable pageable);
    Page<DN_Content> findByStatusAndAuthorContainingIgnoreCase(String status, String author, Pageable pageable);
    
    // Tag-based searches
    Page<DN_Content> findByStatusAndTagsContaining(String status, String tag, Pageable pageable);
    
    // Featured content
    Page<DN_Content> findByStatusAndFeatured(String status, boolean featured, Pageable pageable);
    
    // Content title searches for suggestions
    List<DN_Content> findTop10ByStatusAndContentTitleContainingIgnoreCase(String status, String title);
    
    // Related content
    Page<DN_Content> findByStatusAndContentCategoriesAndContentIdNot(
        String status, OsEnum category, int excludeId, Pageable pageable);
    
    // Slug-based methods
    boolean existsBySlug(String slug);
    boolean existsBySlugAndContentIdNot(String slug, int contentId);
    Optional<DN_Content> findBySlug(String slug);
    
    // Count methods
    long countByStatus(String status);
    long countByAuthor(String author);
    long countByStatusAndContentCategories(String status, OsEnum category);
    
    // Admin methods
    Page<DN_Content> findByContentTitleContainingIgnoreCase(String title, Pageable pageable);
    
    // Date-based queries
    @Query("SELECT c FROM DN_Content c WHERE c.status = :status AND c.publishedDate BETWEEN :startDate AND :endDate ORDER BY c.publishedDate DESC")
    Page<DN_Content> findByStatusAndPublishedDateBetween(
        @Param("status") String status, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate, 
        Pageable pageable);
    
    // Popular content by views
    @Query("SELECT c FROM DN_Content c WHERE c.status = :status ORDER BY c.views DESC")
    Page<DN_Content> findByStatusOrderByViewsDesc(@Param("status") String status, Pageable pageable);
    
    // Content with most comments
    @Query("SELECT c FROM DN_Content c WHERE c.status = :status ORDER BY SIZE(c.comments) DESC")
    Page<DN_Content> findByStatusOrderByCommentCountDesc(@Param("status") String status, Pageable pageable);
    
    // Search in content body
    @Query("SELECT c FROM DN_Content c WHERE c.status = :status AND (c.contentTitle LIKE %:keyword% OR c.contentBody LIKE %:keyword% OR c.tags LIKE %:keyword%)")
    Page<DN_Content> findByStatusAndFullTextSearch(@Param("status") String status, @Param("keyword") String keyword, Pageable pageable);
    
    // Recent content by category
    @Query("SELECT c FROM DN_Content c WHERE c.status = :status AND c.contentCategories = :category ORDER BY c.publishedDate DESC")
    List<DN_Content> findRecentByCategory(@Param("status") String status, @Param("category") OsEnum category, Pageable pageable);
    
    // Statistics queries
    @Query("SELECT COUNT(c) FROM DN_Content c WHERE c.status = 'PUBLISHED' AND c.publishedDate >= :date")
    long countPublishedSince(@Param("date") LocalDateTime date);
    
    @Query("SELECT c.contentCategories, COUNT(c) FROM DN_Content c WHERE c.status = 'PUBLISHED' GROUP BY c.contentCategories")
    List<Object[]> getContentCountByCategory();
    
    // Archive methods
    @Query("SELECT YEAR(c.publishedDate), MONTH(c.publishedDate), COUNT(c) FROM DN_Content c WHERE c.status = 'PUBLISHED' GROUP BY YEAR(c.publishedDate), MONTH(c.publishedDate) ORDER BY YEAR(c.publishedDate) DESC, MONTH(c.publishedDate) DESC")
    List<Object[]> getMonthlyArchive();
    
    // Draft methods for authors
    Page<DN_Content> findByStatusAndAuthor(String status, String author, Pageable pageable);
}
