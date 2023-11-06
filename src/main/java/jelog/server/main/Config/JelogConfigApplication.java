package jelog.server.main.Config;

import jelog.server.main.Global.Jwt.CustomAccessDeniedHandler;
import jelog.server.main.Global.Jwt.JwtAuthenticationFilter;
import jelog.server.main.Global.Jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.sql.DataSource;


/**
 * Description :
 * PackageName : jelog.server.main.Config
 * FileName : JelogConfigApplication
 * Author : MinJaeYoung
 * TimeDate : 2023-06-02
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-06-02               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 * @Configuration 설정파일 세팅을 위한 어노텐션, Bean 등록등.
 * @EnableTransactionManagement 트랜잭션 허용하는 어노텐션.
 * @EnableWebSecurity 적용 완료.
 */
@Configuration
@MapperScan(value = {"jelog.server.main.Mapper"})
@EnableTransactionManagement
@EnableWebSecurity
@RequiredArgsConstructor
public class JelogConfigApplication extends WebSecurityConfigurerAdapter{
    /**
     * Bane Setting
     * */

    /**
     * [Variables]
     * @authIgnoring ignoring Setting
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------

    private final JwtProvider jwtProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    /**     * WebSecurity
     * @param web WebSecurity
     */
    //-------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * HttpSecurity
     * @param http Web Security Setting
     */
    //-------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .httpBasic()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/api/public/**","/api/ko-jy/**","/api/view/public/**", "/", "/templates/**").permitAll()
                .antMatchers("/api/auth/**","/api/view/auth/**").access("hasRole('USER') or hasRole('ADMIN')")
                .antMatchers("/api/republic/**", "/api/view/republic/**").hasRole("ADMIN")
                .antMatchers("/static/images/**","/static/css/**", "/static/js/**", "/static/favicon/**", "/favicon.ico").permitAll() // Allow access to CSS and JS files in the /static/ folder
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * PasswordEncoder
     * @return PasswordEncoder
     */
    //-------------------------------------------------------------------------------------------------------------------------------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Sql data Base 쿼리 매퍼 사용시.
     * @return Sql data Base 쿼리 매퍼 사용시.
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
//    @Lazy
    @Bean
    public SqlSessionFactory sqlSession(DataSource dataSource) throws Exception{
        SqlSessionFactoryBean sqlOptions = new SqlSessionFactoryBean();
        sqlOptions.setDataSource(dataSource);
        Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath*:Mapper/*_SQL.xml");
        sqlOptions.setMapperLocations(res);
        return sqlOptions.getObject();
    }

    //-------------------------------------------------------------------------------------------------------------------------------------
    @Bean
    public SqlSessionTemplate sqlTemplate(SqlSessionFactory sqlSessionFactory) throws Exception{
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * File Option 파일서비스 담당
     * @return multipartResolver
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver fileOptions = new CommonsMultipartResolver();
        fileOptions.setDefaultEncoding("UTF-8");
        fileOptions.setMaxUploadSizePerFile(1024 * 1024 * 5);
        return fileOptions;
    }
}
