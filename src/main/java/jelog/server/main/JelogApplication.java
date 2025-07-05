package jelog.server.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @EnableScheduling 스케줄러 등록 사용예정.
 * TODO. @EnableScheduling. Ex) Remove with 1-Year User Data... 로직 추가등 유용하게 활용 가능.
 * @EnableAspectJAutoProxy 오토프로시
 * @EnableCaching 캐싱 활성화
 * */

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
@EnableCaching
public class JelogApplication {

    public static void main(String[] args) {
        SpringApplication.run(JelogApplication.class, args);
    }

}
