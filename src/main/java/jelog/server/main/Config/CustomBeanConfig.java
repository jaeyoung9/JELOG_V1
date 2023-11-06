package jelog.server.main.Config;

import jelog.server.main.Global.Jwt.CustomAccessDeniedHandler;
import jelog.server.main.Global.Jwt.JwtProvider;
import jelog.server.main.Service.Jwt.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;


/**
 * Description :
 * PackageName : jelog.server.main.Config
 * FileName : CustomBeanConfig
 * Author : User
 * TimeDate : 2023-11-05
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-11-05               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */
@Configuration
public class CustomBeanConfig {


    private JpaUserDetailsService jpaUserDetailsService;

    @Lazy
    @Autowired
    public void customBeanConfig(JpaUserDetailsService _jpaUserDetailsService){
        this.jpaUserDetailsService = _jpaUserDetailsService;
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

}
