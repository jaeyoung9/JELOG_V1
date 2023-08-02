package jelog.server.main.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Description :
 * PackageName : jelog.server.main.Config
 * FileName : JelogMvcConfigApplication
 * Author : MinJaeYoung
 * TimeDate : 2023-08-02
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-08-02               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */
@Configuration
public class JelogMvcConfigApplication implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"));
    }
}
