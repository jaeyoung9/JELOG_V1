package jelog.server.main.Service;

import jelog.server.main.Dto.DT_Content;
import jelog.server.main.Dto.DT_Files;
import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Global.ValidationUtils;
import jelog.server.main.Model.DN_Content;
import jelog.server.main.Model.DN_Files;
import jelog.server.main.Repositories.DN_CommentRepositories;
import jelog.server.main.Repositories.DN_ContentRepositories;
import jelog.server.main.Repositories.DN_FilesRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description :
 * PackageName : jelog.server.main.Service
 * FileName : DN_ContentService
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
public class DN_ContentService {

    /**
     * [Variables]
     * Repository
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------

    private final DN_FilesRepositories dn_filesRepositories;
    private final DN_CommentRepositories dn_commentRepositories;
    private final DN_ContentRepositories dn_contentRepositories;

    @Autowired
    public DN_ContentService(DN_FilesRepositories dn_filesRepositories, DN_CommentRepositories dn_commentRepositories, DN_ContentRepositories dn_contentRepositories){
        this.dn_filesRepositories = dn_filesRepositories;
        this.dn_commentRepositories = dn_commentRepositories;
        this.dn_contentRepositories = dn_contentRepositories;
    }

    /**
     * [Content]
     * Validate
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    public void validateExistContentId(int contentId){
        boolean data = dn_contentRepositories.existsById(contentId);
        if(true != data){
            log.warn("not exist contentId");
            throw  new RuntimeException("not exist contentId");
        }
    }

    public void validateContent(final DT_Content _model){
        if(null == _model){
            log.warn("data is check for cannot be null.");
            throw new RuntimeException("data is check for cannot be null.");
        }
    }

    /**
     * [Content]
     * Content Services
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------

    /**
     * [Content] Create Content
     * @param entity 게시글 데이터
     * @return DN_Content content 데이터
     * */
    @Transactional
    @CacheEvict(value = "contents", allEntries = true)
    public DN_Content createContent(DT_Content entity){
        validateContent(entity);
        
        // Generate slug if not provided
        entity.generateSlugFromTitle();
        
        // Ensure unique slug
        entity.setSlug(generateUniqueSlug(entity.getSlug()));
        
        // Set default values
        if (entity.getStatus() == null) {
            entity.setStatus("PUBLISHED");
        }
        
        if ("PUBLISHED".equals(entity.getStatus()) && entity.getPublishedDate() == null) {
            entity.setPublishedDate(LocalDateTime.now());
        }

        // Convert DTO to Entity
        DN_Content content = DT_Content.toEntity(entity);
        content = dn_contentRepositories.save(content);

        // Create and save files if present
        List<DT_Files> dtFilesList = entity.getFiles();
        if(dtFilesList != null && !dtFilesList.isEmpty()){
            for(DT_Files dtFile : dtFilesList){
                DN_Files saveFile = new DN_Files();
                saveFile.setFilePath(dtFile.getFilePath());
                saveFile.setFileName(dtFile.getFileName());
                saveFile.setMediaType(dtFile.getMediaType());
                saveFile.setResultFile(dtFile.getResultFile());
                saveFile.setContentId(content.getContentId());
                saveFile.setContent(content);
                dn_filesRepositories.save(saveFile);
            }
        }
        
        log.info("Created new content with ID: {}, Title: {}", content.getContentId(), content.getContentTitle());
        return content;
    }
    
    /**
     * Update existing content
     */
    @Transactional
    @CacheEvict(value = "contents", allEntries = true)
    public DN_Content updateContent(DT_Content entity) {
        validateContent(entity);
        
        DN_Content existingContent = dn_contentRepositories.findById(entity.getContentId())
            .orElseThrow(() -> new EntityNotFoundException("Content not found with ID: " + entity.getContentId()));
        
        // Update fields
        existingContent.setContentTitle(entity.getContentTitle());
        existingContent.setContentBody(entity.getContentBody());
        existingContent.setContentSummary(entity.getContentSummary());
        existingContent.setContentCategories(entity.getContentCategories());
        existingContent.setContentThumbnail(entity.getContentThumbnail());
        existingContent.setMetaDescription(entity.getMetaDescription());
        existingContent.setMetaKeywords(entity.getMetaKeywords());
        existingContent.setStatus(entity.getStatus());
        existingContent.setFeatured(entity.isFeatured());
        existingContent.setAllowComments(entity.isAllowComments());
        
        if (entity.getTags() != null) {
            existingContent.setTagSet(entity.getTags());
        }
        
        // Update slug if title changed
        if (!existingContent.getContentTitle().equals(entity.getContentTitle())) {
            entity.generateSlugFromTitle();
            existingContent.setSlug(generateUniqueSlug(entity.getSlug(), existingContent.getContentId()));
        }
        
        // Update published date if status changed to PUBLISHED
        if ("PUBLISHED".equals(entity.getStatus()) && existingContent.getPublishedDate() == null) {
            existingContent.setPublishedDate(LocalDateTime.now());
        }
        
        return dn_contentRepositories.save(existingContent);
    }
    
    /**
     * Save as draft
     */
    @Transactional
    public DN_Content saveDraft(DT_Content entity) {
        entity.setStatus("DRAFT");
        return createContent(entity);
    }
    
    /**
     * Publish content
     */
    @Transactional
    @CacheEvict(value = "contents", allEntries = true)
    public DN_Content publishContent(int contentId) {
        DN_Content content = findContent(contentId);
        content.setStatus("PUBLISHED");
        content.setPublishedDate(LocalDateTime.now());
        return dn_contentRepositories.save(content);
    }
    
    /**
     * Delete content
     */
    @Transactional
    @CacheEvict(value = "contents", allEntries = true)
    public void deleteContent(int contentId) {
        DN_Content content = findContent(contentId);
        content.setStatus("DELETED");
        dn_contentRepositories.save(content);
    }
    
    /**
     * Permanently delete content (admin only)
     */
    @Transactional
    @CacheEvict(value = "contents", allEntries = true)
    public void deleteContentPermanently(int contentId) {
        dn_contentRepositories.deleteById(contentId);
    }

    /**
     * [Content] Main Content Page
     * */
    public Page<?> findPage(Pageable pageable, String Title, OsEnum Categories){
        
        // Always search for PUBLISHED content only
        String status = "PUBLISHED";
        
        // Check if we have a title search
        boolean hasTitle = Title != null && !Title.trim().isEmpty();
        
        // Use appropriate repository method based on parameters
        if (hasTitle && Categories != null) {
            // Search by title and category
            return dn_contentRepositories.findByStatusAndContentTitleContainingIgnoreCaseAndContentCategories(
                status, Title.trim(), Categories, pageable);
        } else if (hasTitle) {
            // Search by title only
            return dn_contentRepositories.findByStatusAndContentTitleContainingIgnoreCase(
                status, Title.trim(), pageable);
        } else if (Categories != null) {
            // Filter by category only
            return dn_contentRepositories.findByStatusAndContentCategories(
                status, Categories, pageable);
        } else {
            // Get all published content, ordered by published date
            return dn_contentRepositories.findByStatusOrderByPublishedDateDesc(
                status, pageable);
        }
    }

    /**
     * [Content] Main Content Detail
     * */
    @Transactional
    public DN_Content findContent(int mainNumber){

        validateExistContentId(mainNumber);

        return dn_contentRepositories.findByContentId(mainNumber);
    }

    /**
     * [Content] View Count
     * */
    @Transactional
    public String contentViewCount(int contentNumber){
        validateExistContentId(contentNumber);
        DN_Content entity = dn_contentRepositories.findByContentId(contentNumber);
        entity.setViews(entity.getViews() + 1);
        dn_contentRepositories.save(entity);
        return "";
    }
    
    /**
     * Increment view count
     */
    @Transactional
    public void incrementViewCount(int contentId) {
        DN_Content content = findContent(contentId);
        content.setViews(content.getViews() + 1);
        dn_contentRepositories.save(content);
    }
    
    /**
     * Find published posts with search
     */
    @Cacheable(value = "contents", key = "#pageable.pageNumber + '_' + #keyword + '_' + #category")
    public Page<DN_Content> findPublishedPosts(Pageable pageable, String keyword, String category) {
        if (StringUtils.hasText(keyword)) {
            if (StringUtils.hasText(category)) {
                OsEnum categoryEnum = OsEnum.valueOf(category);
                return dn_contentRepositories.findByStatusAndContentTitleContainingIgnoreCaseAndContentCategories(
                    "PUBLISHED", keyword, categoryEnum, pageable);
            } else {
                return dn_contentRepositories.findByStatusAndContentTitleContainingIgnoreCaseOrContentBodyContainingIgnoreCase(
                    "PUBLISHED", keyword, keyword, pageable);
            }
        } else if (StringUtils.hasText(category)) {
            OsEnum categoryEnum = OsEnum.valueOf(category);
            return dn_contentRepositories.findByStatusAndContentCategories("PUBLISHED", categoryEnum, pageable);
        } else {
            return dn_contentRepositories.findByStatusOrderByPublishedDateDesc("PUBLISHED", pageable);
        }
    }
    
    /**
     * Find published post by ID
     */
    @Cacheable(value = "content", key = "#contentId")
    public DN_Content findPublishedPost(int contentId) {
        return dn_contentRepositories.findByContentIdAndStatus(contentId, "PUBLISHED")
            .orElse(null);
    }
    
    /**
     * Search posts with advanced criteria
     */
    public Page<DN_Content> searchPosts(Pageable pageable, String keyword, OsEnum category, String sortBy) {
        // Create custom sort if specified
        Sort sort = Sort.by(Sort.Direction.DESC, "publishedDate");
        if ("views".equals(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "views");
        } else if ("likes".equals(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "likes");
        } else if ("title".equals(sortBy)) {
            sort = Sort.by(Sort.Direction.ASC, "contentTitle");
        }
        
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        
        if (category != null) {
            return dn_contentRepositories.findByStatusAndContentTitleContainingIgnoreCaseAndContentCategories(
                "PUBLISHED", keyword != null ? keyword : "", category, sortedPageable);
        } else {
            return dn_contentRepositories.findByStatusAndContentTitleContainingIgnoreCaseOrContentBodyContainingIgnoreCase(
                "PUBLISHED", keyword != null ? keyword : "", keyword != null ? keyword : "", sortedPageable);
        }
    }
    
    /**
     * Find posts by category
     */
    public Page<DN_Content> findByCategory(Pageable pageable, OsEnum category) {
        return dn_contentRepositories.findByStatusAndContentCategories("PUBLISHED", category, pageable);
    }
    
    /**
     * Find posts by tag
     */
    public Page<DN_Content> findByTag(Pageable pageable, String tag) {
        return dn_contentRepositories.findByStatusAndTagsContaining("PUBLISHED", tag, pageable);
    }
    
    /**
     * Find posts by author
     */
    public Page<DN_Content> findPostsByAuthor(Pageable pageable, String author) {
        return dn_contentRepositories.findByAuthorOrderByInDateDesc(author, pageable);
    }
    
    /**
     * Get search suggestions
     */
    public List<String> getSearchSuggestions(String query) {
        List<DN_Content> contents = dn_contentRepositories.findTop10ByStatusAndContentTitleContainingIgnoreCase(
            "PUBLISHED", query);
        return contents.stream()
            .map(DN_Content::getContentTitle)
            .limit(5)
            .collect(Collectors.toList());
    }
    
    /**
     * Find related posts
     */
    public List<DN_Content> findRelatedPosts(int postId, int limit) {
        DN_Content currentPost = findContent(postId);
        if (currentPost == null) return Collections.emptyList();
        
        // Find posts with same category
        Pageable pageable = PageRequest.of(0, limit);
        Page<DN_Content> relatedPosts = dn_contentRepositories.findByStatusAndContentCategoriesAndContentIdNot(
            "PUBLISHED", currentPost.getContentCategories(), postId, pageable);
        
        return relatedPosts.getContent();
    }
    
    /**
     * Advanced search with multiple criteria
     */
    public Page<DN_Content> advancedSearch(Pageable pageable, String keyword, String category, 
                                         String dateFrom, String dateTo, String author) {
        // This would typically use Criteria API or custom repository method
        // For now, implementing basic search
        if (StringUtils.hasText(author)) {
            return dn_contentRepositories.findByStatusAndAuthorContainingIgnoreCase("PUBLISHED", author, pageable);
        } else if (StringUtils.hasText(keyword)) {
            return searchPosts(pageable, keyword, null, null);
        } else {
            return findPublishedPosts(pageable, null, null);
        }
    }
    
    /**
     * Get popular posts by views
     */
    public List<DN_Content> getPopularPosts(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "views"));
        return dn_contentRepositories.findByStatus("PUBLISHED", pageable).getContent();
    }
    
    /**
     * Get recent posts
     */
    public List<DN_Content> getRecentPosts(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "publishedDate"));
        return dn_contentRepositories.findByStatus("PUBLISHED", pageable).getContent();
    }
    
    /**
     * Get featured posts
     */
    public List<DN_Content> getFeaturedPosts(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "publishedDate"));
        return dn_contentRepositories.findByStatusAndFeatured("PUBLISHED", true, pageable).getContent();
    }
    
    /**
     * Get total post count
     */
    public long getTotalPostCount() {
        return dn_contentRepositories.countByStatus("PUBLISHED");
    }
    
    /**
     * Get total views across all published posts
     */
    public long getTotalViews() {
        return dn_contentRepositories.sumViewsByStatus("PUBLISHED");
    }
    
    /**
     * Get posts for admin (all statuses)
     */
    public Page<DN_Content> getPostsForAdmin(Pageable pageable, String title, String category, String status) {
        if (StringUtils.hasText(status) && StringUtils.hasText(title) && StringUtils.hasText(category)) {
            OsEnum categoryEnum = OsEnum.valueOf(category);
            return dn_contentRepositories.findByStatusAndContentTitleContainingIgnoreCaseAndContentCategories(
                status, title, categoryEnum, pageable);
        } else if (StringUtils.hasText(status) && StringUtils.hasText(title)) {
            return dn_contentRepositories.findByStatusAndContentTitleContainingIgnoreCase(status, title, pageable);
        } else if (StringUtils.hasText(status) && StringUtils.hasText(category)) {
            OsEnum categoryEnum = OsEnum.valueOf(category);
            return dn_contentRepositories.findByStatusAndContentCategories(status, categoryEnum, pageable);
        } else if (StringUtils.hasText(status)) {
            return dn_contentRepositories.findByStatus(status, pageable);
        } else if (StringUtils.hasText(title)) {
            return dn_contentRepositories.findByContentTitleContainingIgnoreCase(title, pageable);
        } else {
            return dn_contentRepositories.findAll(pageable);
        }
    }
    
    /**
     * Update post status
     */
    @Transactional
    @CacheEvict(value = "contents", allEntries = true)
    public void updatePostStatus(int postId, String status) {
        DN_Content content = findContent(postId);
        content.setStatus(status);
        if ("PUBLISHED".equals(status) && content.getPublishedDate() == null) {
            content.setPublishedDate(LocalDateTime.now());
        }
        dn_contentRepositories.save(content);
    }
    
    /**
     * Delete post by admin
     */
    @Transactional
    @CacheEvict(value = "contents", allEntries = true)
    public void deletePost(int postId) {
        dn_contentRepositories.deleteById(postId);
    }
    
    /**
     * Generate unique slug
     */
    private String generateUniqueSlug(String baseSlug) {
        return generateUniqueSlug(baseSlug, null);
    }
    
    private String generateUniqueSlug(String baseSlug, Integer excludeId) {
        String slug = baseSlug;
        int counter = 1;
        
        while (isSlugExists(slug, excludeId)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        
        return slug;
    }
    
    /**
     * Check if slug exists
     */
    private boolean isSlugExists(String slug, Integer excludeId) {
        if (excludeId != null) {
            return dn_contentRepositories.existsBySlugAndContentIdNot(slug, excludeId);
        } else {
            return dn_contentRepositories.existsBySlug(slug);
        }
    }
}
