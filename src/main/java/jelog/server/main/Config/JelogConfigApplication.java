package jelog.server.main.Config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import javax.sql.DataSource;

/**
 * @Configuration 설정파일 세팅을 위한 어노텐션, Bean 등록등.
 * @EnableTransactionManagement 트랜잭션 허용하는 어노텐션.
 * @EnableWebSecurity 현재 미적용 차후 토큰 활용시 적용 예정.
 * */
@Configuration
@MapperScan(value = {"jelog.server.main.Mapper"})
@EnableTransactionManagement
//@EnableWebSecurity
public class JelogConfigApplication {


    /**
     * Bane 설정
     * */

    // TODO: 1. Web Security 개발 예정 실험필요
    /**
     * passwordEncoder
     * @return PassWord Encoder
     * 패스워드 함호화 적용
     * */
//    @Bean
//    public static PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    /**
     * webSecurityCustomizer
     * @return ignoring
     * 스프링 부트 기본제공하는 static 리소스들의 기본 위치를 가져와 스프링 시큐리티에서 제외시킨다.
     * */
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }

    /**
     * SecurityFilterChain
     *
     * */

    /**
     * Sql data Base 쿼리 매퍼 사용시.
     * @return Sql data Base 쿼리 매퍼 사용시.
     * */
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

    /**
     * File Option 파일서비스 담당
     * @return multipartResolver
     * */ 
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver fileOptions = new CommonsMultipartResolver();
        fileOptions.setDefaultEncoding("UTF-8");
        fileOptions.setMaxUploadSizePerFile(1024 * 1024 * 5);
        return fileOptions;
    }

    /**
     * Jsp 사용 할경우. 
     * @return InternalResourceViewResolver
     */
//    @Bean
//    public ViewResolver views(){
//        return new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");
//    }

}
