package jelog.server.main.Service;

import jelog.server.main.Model.DN_Category;
import jelog.server.main.Repositories.DN_CategoryRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Description : Dynamic Category Service
 * PackageName : jelog.server.main.Service
 * FileName : DN_CategoryService
 * Author : MinJaeYoung
 * TimeDate : 2025-07-05
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2025-07-05               MinJaeYoung                     최초생성
 * ------------------------------------------------------------
 */

@Slf4j
@Service
public class DN_CategoryService {

    private final DN_CategoryRepositories categoryRepository;

    @Autowired
    public DN_CategoryService(DN_CategoryRepositories categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Get all active categories
     */
    public List<DN_Category> getAllActiveCategories() {
        return categoryRepository.findActiveCategories();
    }

    /**
     * Get featured categories
     */
    public List<DN_Category> getFeaturedCategories() {
        return categoryRepository.findFeaturedCategories();
    }

    /**
     * Get categories with posts
     */
    public List<DN_Category> getCategoriesWithPosts() {
        return categoryRepository.findCategoriesWithPosts();
    }

    /**
     * Get category by slug
     */
    public Optional<DN_Category> getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    /**
     * Get category by ID
     */
    public Optional<DN_Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * Search categories
     */
    public List<DN_Category> searchCategories(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return getAllActiveCategories();
        }
        return categoryRepository.searchCategories(keyword.trim());
    }

    /**
     * Get root categories (no parent)
     */
    public List<DN_Category> getRootCategories() {
        return categoryRepository.findRootCategories("ACTIVE");
    }

    /**
     * Get subcategories
     */
    public List<DN_Category> getSubcategories(Long parentId) {
        return categoryRepository.findByParentIdAndStatusOrderBySortOrderAscNameAsc(parentId, "ACTIVE");
    }

    /**
     * Get category hierarchy
     */
    public List<DN_Category> getCategoryHierarchy() {
        return categoryRepository.getCategoryHierarchy();
    }

    /**
     * Get recent categories
     */
    public List<DN_Category> getRecentCategories(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return categoryRepository.findRecentCategories(pageable);
    }

    /**
     * Admin: Get categories for admin panel
     */
    public Page<DN_Category> getCategoriesForAdmin(Pageable pageable, String name, String status, Long parentId) {
        return categoryRepository.findCategoriesForAdmin(name, status, parentId, pageable);
    }

    /**
     * Admin: Create category
     */
    @Transactional
    public DN_Category createCategory(DN_Category category) {
        // Generate slug if not provided
        if (!StringUtils.hasText(category.getSlug())) {
            category.setSlug(generateSlugFromName(category.getName()));
        }
        
        // Ensure unique slug
        category.setSlug(generateUniqueSlug(category.getSlug()));
        
        // Set default values
        if (!StringUtils.hasText(category.getStatus())) {
            category.setStatus("ACTIVE");
        }
        
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        
        if (category.getFeatured() == null) {
            category.setFeatured(false);
        }
        
        if (category.getPostCount() == null) {
            category.setPostCount(0);
        }

        DN_Category savedCategory = categoryRepository.save(category);
        log.info("Created new category: {} (ID: {})", savedCategory.getName(), savedCategory.getId());
        
        return savedCategory;
    }

    /**
     * Admin: Update category
     */
    @Transactional
    public DN_Category updateCategory(Long id, DN_Category categoryData) {
        DN_Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

        // Update fields
        existingCategory.setName(categoryData.getName());
        existingCategory.setDescription(categoryData.getDescription());
        existingCategory.setIcon(categoryData.getIcon());
        existingCategory.setColor(categoryData.getColor());
        existingCategory.setParentId(categoryData.getParentId());
        existingCategory.setSortOrder(categoryData.getSortOrder());
        existingCategory.setStatus(categoryData.getStatus());
        existingCategory.setFeatured(categoryData.getFeatured());
        existingCategory.setMetaTitle(categoryData.getMetaTitle());
        existingCategory.setMetaDescription(categoryData.getMetaDescription());
        existingCategory.setMetaKeywords(categoryData.getMetaKeywords());
        existingCategory.setUpdatedBy(categoryData.getUpdatedBy());

        // Update slug if name changed
        if (!existingCategory.getName().equals(categoryData.getName())) {
            String newSlug = generateSlugFromName(categoryData.getName());
            existingCategory.setSlug(generateUniqueSlug(newSlug, id));
        }

        DN_Category updatedCategory = categoryRepository.save(existingCategory);
        log.info("Updated category: {} (ID: {})", updatedCategory.getName(), updatedCategory.getId());
        
        return updatedCategory;
    }

    /**
     * Admin: Delete category (soft delete)
     */
    @Transactional
    public void deleteCategory(Long id) {
        DN_Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

        category.setStatus("DELETED");
        categoryRepository.save(category);
        
        log.info("Deleted category: {} (ID: {})", category.getName(), category.getId());
    }

    /**
     * Admin: Permanently delete category
     */
    @Transactional
    public void permanentlyDeleteCategory(Long id) {
        categoryRepository.deleteById(id);
        log.info("Permanently deleted category with ID: {}", id);
    }

    /**
     * Update post count for category
     */
    @Transactional
    public void updatePostCount(Long categoryId, int increment) {
        categoryRepository.updatePostCount(categoryId, increment);
    }

    /**
     * Increment post count
     */
    @Transactional
    public void incrementPostCount(Long categoryId) {
        updatePostCount(categoryId, 1);
    }

    /**
     * Decrement post count
     */
    @Transactional
    public void decrementPostCount(Long categoryId) {
        updatePostCount(categoryId, -1);
    }

    /**
     * Get category statistics
     */
    public long getActiveCategoryCount() {
        return categoryRepository.countActiveCategories();
    }

    public Long getTotalPostCount() {
        return categoryRepository.getTotalPostCount();
    }

    /**
     * Generate slug from name
     */
    private String generateSlugFromName(String name) {
        if (!StringUtils.hasText(name)) {
            return "category";
        }
        
        return name.toLowerCase()
                   .replaceAll("[^a-z0-9가-힣]", "-")
                   .replaceAll("-+", "-")
                   .replaceAll("^-|-$", "");
    }

    /**
     * Generate unique slug
     */
    private String generateUniqueSlug(String baseSlug) {
        return generateUniqueSlug(baseSlug, null);
    }

    private String generateUniqueSlug(String baseSlug, Long excludeId) {
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
    private boolean isSlugExists(String slug, Long excludeId) {
        if (excludeId != null) {
            return categoryRepository.existsBySlugAndIdNot(slug, excludeId);
        } else {
            return categoryRepository.existsBySlug(slug);
        }
    }

    /**
     * Initialize default categories
     */
    @Transactional
    public void initializeDefaultCategories() {
        if (categoryRepository.count() > 0) {
            log.info("Categories already exist, skipping initialization");
            return;
        }

        log.info("Initializing default categories...");

        // Create default categories based on existing enums
        createDefaultCategory("Java", "java", "Java 프로그래밍 언어", "fab fa-java", "#f89820", 1);
        createDefaultCategory("JavaScript", "javascript", "JavaScript 프로그래밍 언어", "fab fa-js-square", "#f7df1e", 2);
        createDefaultCategory("Python", "python", "Python 프로그래밍 언어", "fab fa-python", "#3776ab", 3);
        createDefaultCategory("C/C++", "c-cpp", "C/C++ 프로그래밍 언어", "fas fa-code", "#00599c", 4);
        createDefaultCategory("Shell Script", "shell", "Shell 스크립팅", "fas fa-terminal", "#4eaa25", 5);
        createDefaultCategory("보안", "security", "정보 보안 관련", "fas fa-shield-alt", "#dc3545", 6);
        createDefaultCategory("개발자 커리어", "career", "개발자 커리어 스킬", "fas fa-briefcase", "#6f42c1", 7);
        createDefaultCategory("기타", "other", "기타 카테고리", "fas fa-folder", "#6c757d", 8);

        log.info("Default categories initialized successfully");
    }

    private void createDefaultCategory(String name, String slug, String description, String icon, String color, int sortOrder) {
        DN_Category category = DN_Category.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .icon(icon)
                .color(color)
                .sortOrder(sortOrder)
                .status("ACTIVE")
                .featured(false)
                .postCount(0)
                .createdBy("SYSTEM")
                .build();

        categoryRepository.save(category);
        log.info("Created default category: {}", name);
    }
}