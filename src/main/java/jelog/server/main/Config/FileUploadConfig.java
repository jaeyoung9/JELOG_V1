package jelog.server.main.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * Description : File Upload Configuration
 * PackageName : jelog.server.main.Config
 * FileName : FileUploadConfig
 * Author : MinJaeYoung
 * TimeDate : 2024-01-01
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2024-01-01               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@Configuration
@EnableTransactionManagement
public class FileUploadConfig {

    /**
     * File Upload Configuration
     * @return multipartResolver
     */
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver fileOptions = new CommonsMultipartResolver();
        fileOptions.setDefaultEncoding("UTF-8");
        fileOptions.setMaxUploadSizePerFile(1024 * 1024 * 5); // 5MB per file
        fileOptions.setMaxUploadSize(1024 * 1024 * 25); // 25MB total
        return fileOptions;
    }
}