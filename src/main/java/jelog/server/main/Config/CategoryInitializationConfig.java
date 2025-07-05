package jelog.server.main.Config;

import jelog.server.main.Service.CategoryMigrationService;
import jelog.server.main.Service.DN_CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description : Auto-initialize categories on application startup
 * PackageName : jelog.server.main.Config
 * FileName : CategoryInitializationConfig
 * Author : MinJaeYoung
 * TimeDate : 2025-07-05
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2025-07-05               MinJaeYoung                     최초생성
 * ------------------------------------------------------------
 */

@Slf4j
@Configuration
public class CategoryInitializationConfig {

    @Autowired
    private DN_CategoryService categoryService;

    @Autowired
    private CategoryMigrationService migrationService;

    @Bean
    public CommandLineRunner initializeCategories() {
        return args -> {
            try {
                log.info("Checking if category initialization is needed...");
                
                if (migrationService.isMigrationNeeded()) {
                    log.info("Category migration needed, performing initialization...");
                    migrationService.performCompleteMigration();
                    log.info("Category initialization completed successfully");
                } else {
                    log.info("Categories already initialized, skipping migration");
                }
                
            } catch (Exception e) {
                log.error("Failed to initialize categories: {}", e.getMessage(), e);
                // Don't fail application startup, just log the error
            }
        };
    }
}