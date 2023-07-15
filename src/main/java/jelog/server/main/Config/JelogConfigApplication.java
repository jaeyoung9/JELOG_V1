package jelog.server.main.Config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;

import javax.sql.DataSource;

/**
 * @Configuration 설정파일 세팅을 위한 어노텐션, Bean 등록등.
 * @EnableTransactionManagement 트랜잭션 허용하는 어노텐션.
 * */
@Configuration
@MapperScan(value = {"jelog.server.main.Mapper"})
@EnableTransactionManagement
public class JelogConfigApplication {


    /**
     * Bane 설정
     * */

    // 1. Sql data Base 쿼리 매퍼 사용시.
    @Bean
    public SqlSessionFactory sqlSession(DataSource dataSource) throws Exception{
        SqlSessionFactoryBean sqlOptions = new SqlSessionFactoryBean();
        sqlOptions.setDataSource(dataSource);
        Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath:Mapper/*_SQL.xml");
        sqlOptions.setMapperLocations(res);
        return sqlOptions.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlTemplate(SqlSessionFactory sqlSessionFactory) throws Exception{
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    // TODO: 2. Web Security 개발 예정 실험필요

    // 3. File 빈등록
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver fileOptions = new CommonsMultipartResolver();
        fileOptions.setDefaultEncoding("UTF-8");
        fileOptions.setMaxUploadSizePerFile(1024 * 1024 * 5);
        return fileOptions;
    }

    // 4. Jsp 사용 할경우.
//    @Bean
//    public ViewResolver views(){
//        return new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");
//    }
}
