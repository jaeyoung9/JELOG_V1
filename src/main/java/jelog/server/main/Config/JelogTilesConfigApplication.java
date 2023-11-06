package jelog.server.main.Config;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.sql.DataSource;

/**
 * Description :
 * PackageName : jelog.server.main.Config
 * FileName : JelogTilesConfigApplication
 * Author : MinJaeYoung
 * TimeDate : 2023-08-05
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-08-05               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@Configuration
public class JelogTilesConfigApplication implements WebMvcConfigurer {

//    @Bean
//    public TilesConfigurer tilesConfigurer() {
//        TilesConfigurer configurer = new TilesConfigurer();
//        configurer.setDefinitions("classpath*:/WEB-INF/Tiles/tiles.xml");
//        configurer.setCheckRefresh(true);
//        return configurer;
//    }
//
//    @Bean
//    public TilesViewResolver tilesViewResolver() {
//        final TilesViewResolver resolver = new TilesViewResolver();
//        resolver.setViewClass(TilesView.class);
//        return resolver;
//    }

//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        TilesViewResolver viewResolver = new TilesViewResolver();
//        viewResolver.setOrder(0);
//        registry.viewResolver(viewResolver);
//
//        InternalResourceViewResolver jspViewResolver = new InternalResourceViewResolver();
//        jspViewResolver.setPrefix("/WEB-INF/views/");
//        jspViewResolver.setSuffix(".jsp");
//        jspViewResolver.setOrder(1);
//        registry.viewResolver(jspViewResolver);
//    }


    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());

        return templateEngine;
    }

    @Bean
    public ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/favicon.ico").setViewName("forward:/static/favicon/favicon.ico");
//    }

//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://localhost:3306/jelog?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
//        dataSource.setUsername("root");
//        dataSource.setPassword("qwe123");
//        return dataSource;
//    }

}
