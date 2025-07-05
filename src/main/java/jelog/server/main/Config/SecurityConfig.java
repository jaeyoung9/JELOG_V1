package jelog.server.main.Config;

import jelog.server.main.Global.Jwt.CustomAccessDeniedHandler;
import jelog.server.main.Global.Jwt.JwtAuthenticationFilter;
import jelog.server.main.Global.Jwt.JwtProvider;
import jelog.server.main.Service.Jwt.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Description : 보안 설정 클래스
 * PackageName : jelog.server.main.Config
 * FileName : SecurityConfig
 * Author : MinJaeYoung
 * TimeDate : 2024-01-01
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2024-01-01               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final JpaUserDetailsService jpaUserDetailsService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public SecurityConfig(JwtProvider jwtProvider, 
                         JpaUserDetailsService jpaUserDetailsService,
                         CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.jwtProvider = jwtProvider;
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 보안 설정
            .csrf().disable()
            
            // CORS 설정
            .cors().configurationSource(corsConfigurationSource())
            
            .and()
            
            // 세션 정책 설정 (JWT 사용)
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                
            .and()
            
            // 보안 헤더 설정
            .headers(headers -> headers
                .frameOptions().deny()
                .contentTypeOptions().and()
                .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                    .maxAgeInSeconds(31536000)
                    .includeSubDomains(true)
                    .preload(true)
                )
                .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                .and()
                .httpPublicKeyPinning(hpkp -> hpkp.disable())
            )
            
            // 요청 권한 설정
            .authorizeRequests(auth -> auth
                .antMatchers("/", "/api/public/**", "/static/**", "/css/**", "/js/**", "/images/**", "/favicon/**").permitAll()
                .antMatchers("/api/view/public/**").permitAll()
                .antMatchers("/api/ko-jy/in/sign/", "/api/ko-jy/up/sign/").permitAll()
                .antMatchers("/api/search/popular-keywords").permitAll()
                .antMatchers("/api/pro").hasRole("USER")
                .antMatchers("/api/view/republic/**", "/api/republic/**", "/api/adm/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            
            // 접근 거부 처리
            .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                
            .and()
            
            // JWT 필터 추가
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}