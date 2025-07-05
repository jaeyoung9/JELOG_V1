package jelog.server.main.Service;

import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Model.DN_Category;
import jelog.server.main.Model.DN_Content;
import jelog.server.main.Repositories.DN_CategoryRepositories;
import jelog.server.main.Repositories.DN_ContentRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description : Category Migration Service for transitioning from OsEnum to DN_Category
 * PackageName : jelog.server.main.Service
 * FileName : CategoryMigrationService
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
public class CategoryMigrationService {

    private final DN_CategoryRepositories categoryRepository;
    private final DN_ContentRepositories contentRepository;
    private final DN_CategoryService categoryService;

    @Autowired
    public CategoryMigrationService(DN_CategoryRepositories categoryRepository,
                                  DN_ContentRepositories contentRepository,
                                  DN_CategoryService categoryService) {
        this.categoryRepository = categoryRepository;
        this.contentRepository = contentRepository;
        this.categoryService = categoryService;
    }

    /**
     * Create default categories from existing enum values
     */
    @Transactional
    public void createDefaultCategoriesFromEnum() {
        log.info("Starting category migration from OsEnum to DN_Category...");

        // Check if categories already exist
        if (categoryRepository.count() > 0) {
            log.info("Categories already exist, skipping creation");
            return;
        }

        Map<OsEnum, DN_Category> createdCategories = new HashMap<>();

        // Create categories for each enum value (excluding non-category enums)
        for (OsEnum osEnum : OsEnum.values()) {
            if (isCategoryEnum(osEnum)) {
                try {
                    DN_Category category = createCategoryFromEnum(osEnum);
                    category = categoryRepository.save(category);
                    createdCategories.put(osEnum, category);
                    log.info("Created category: {} (ID: {}) from enum: {}", 
                            category.getName(), category.getId(), osEnum.name());
                } catch (Exception e) {
                    log.error("Failed to create category from enum: {}", osEnum.name(), e);
                }
            }
        }

        log.info("Successfully created {} categories from enum values", createdCategories.size());
    }

    /**
     * Migrate existing content from enum categories to new category system
     */
    @Transactional
    public void migrateContentCategories() {
        log.info("Starting content category migration...");

        // Find all categories
        List<DN_Category> categories = categoryRepository.findAll();
        Map<String, DN_Category> categoryBySlug = new HashMap<>();
        
        for (DN_Category category : categories) {
            categoryBySlug.put(category.getSlug(), category);
        }

        // Find all content with enum categories but no new category_id
        List<DN_Content> contentList = contentRepository.findAll();
        int migratedCount = 0;
        int skippedCount = 0;

        for (DN_Content content : contentList) {
            try {
                if (content.getCategoryId() == null && content.getContentCategories() != null) {
                    String categorySlug = mapEnumToSlug(content.getContentCategories());
                    DN_Category category = categoryBySlug.get(categorySlug);
                    
                    if (category != null) {
                        content.setCategoryId(category.getId());
                        contentRepository.save(content);
                        
                        // Update post count
                        category.incrementPostCount();
                        categoryRepository.save(category);
                        
                        migratedCount++;
                        log.debug("Migrated content {} to category {}", 
                                content.getContentId(), category.getName());
                    } else {
                        log.warn("No matching category found for enum: {} in content {}", 
                                content.getContentCategories(), content.getContentId());
                        skippedCount++;
                    }
                } else {
                    skippedCount++;
                }
            } catch (Exception e) {
                log.error("Failed to migrate content {}: {}", content.getContentId(), e.getMessage());
                skippedCount++;
            }
        }

        log.info("Content migration completed. Migrated: {}, Skipped: {}", migratedCount, skippedCount);
    }

    /**
     * Check if the migration is needed
     */
    public boolean isMigrationNeeded() {
        long categoryCount = categoryRepository.count();
        long contentWithoutCategoryId = contentRepository.countByContentCategoriesIsNotNullAndCategoryIdIsNull();
        
        return categoryCount == 0 || contentWithoutCategoryId > 0;
    }

    /**
     * Perform complete migration
     */
    @Transactional
    public void performCompleteMigration() {
        log.info("Starting complete category migration...");
        
        // Step 1: Create default categories
        createDefaultCategoriesFromEnum();
        
        // Step 2: Migrate existing content
        migrateContentCategories();
        
        log.info("Complete category migration finished successfully");
    }

    /**
     * Get category by enum (for backward compatibility)
     */
    public DN_Category getCategoryByEnum(OsEnum osEnum) {
        if (osEnum == null) {
            return null;
        }
        
        String slug = mapEnumToSlug(osEnum);
        return categoryRepository.findBySlug(slug).orElse(null);
    }

    /**
     * Map enum to category slug
     */
    private String mapEnumToSlug(OsEnum osEnum) {
        switch (osEnum) {
            case Java:
                return "java";
            case JavaScript:
                return "javascript";
            case Python:
                return "python";
            case C:
                return "c-cpp";
            case Shell:
                return "shell";
            case Security:
                return "security";
            case DeveloperCareerSkills:
                return "career";
            case Other:
            default:
                return "other";
        }
    }

    /**
     * Create category from enum
     */
    private DN_Category createCategoryFromEnum(OsEnum osEnum) {
        String name = getDisplayNameFromEnum(osEnum);
        String slug = mapEnumToSlug(osEnum);
        String description = getDescriptionFromEnum(osEnum);
        String icon = getIconFromEnum(osEnum);
        String color = getColorFromEnum(osEnum);
        int sortOrder = getSortOrderFromEnum(osEnum);

        return DN_Category.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .icon(icon)
                .color(color)
                .sortOrder(sortOrder)
                .status("ACTIVE")
                .featured(false)
                .postCount(0)
                .createdBy("MIGRATION")
                .build();
    }

    /**
     * Check if enum represents a category
     */
    private boolean isCategoryEnum(OsEnum osEnum) {
        switch (osEnum) {
            case Java:
            case JavaScript:
            case Python:
            case C:
            case Shell:
            case Security:
            case DeveloperCareerSkills:
            case Other:
                return true;
            default:
                return false;
        }
    }

    private String getDisplayNameFromEnum(OsEnum osEnum) {
        switch (osEnum) {
            case Java:
                return "Java";
            case JavaScript:
                return "JavaScript";
            case Python:
                return "Python";
            case C:
                return "C/C++";
            case Shell:
                return "Shell Script";
            case Security:
                return "보안";
            case DeveloperCareerSkills:
                return "개발자 커리어";
            case Other:
            default:
                return "기타";
        }
    }

    private String getDescriptionFromEnum(OsEnum osEnum) {
        switch (osEnum) {
            case Java:
                return "Java 프로그래밍 언어 관련 글";
            case JavaScript:
                return "JavaScript 프로그래밍 언어 관련 글";
            case Python:
                return "Python 프로그래밍 언어 관련 글";
            case C:
                return "C/C++ 프로그래밍 언어 관련 글";
            case Shell:
                return "Shell 스크립팅 관련 글";
            case Security:
                return "정보 보안 관련 글";
            case DeveloperCareerSkills:
                return "개발자 커리어 스킬 관련 글";
            case Other:
            default:
                return "기타 카테고리";
        }
    }

    private String getIconFromEnum(OsEnum osEnum) {
        switch (osEnum) {
            case Java:
                return "fab fa-java";
            case JavaScript:
                return "fab fa-js-square";
            case Python:
                return "fab fa-python";
            case C:
                return "fas fa-code";
            case Shell:
                return "fas fa-terminal";
            case Security:
                return "fas fa-shield-alt";
            case DeveloperCareerSkills:
                return "fas fa-briefcase";
            case Other:
            default:
                return "fas fa-folder";
        }
    }

    private String getColorFromEnum(OsEnum osEnum) {
        switch (osEnum) {
            case Java:
                return "#f89820";
            case JavaScript:
                return "#f7df1e";
            case Python:
                return "#3776ab";
            case C:
                return "#00599c";
            case Shell:
                return "#4eaa25";
            case Security:
                return "#dc3545";
            case DeveloperCareerSkills:
                return "#6f42c1";
            case Other:
            default:
                return "#6c757d";
        }
    }

    private int getSortOrderFromEnum(OsEnum osEnum) {
        switch (osEnum) {
            case Java:
                return 1;
            case JavaScript:
                return 2;
            case Python:
                return 3;
            case C:
                return 4;
            case Shell:
                return 5;
            case Security:
                return 6;
            case DeveloperCareerSkills:
                return 7;
            case Other:
            default:
                return 8;
        }
    }
}