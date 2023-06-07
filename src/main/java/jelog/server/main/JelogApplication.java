package jelog.server.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @EnableScheduling 스케줄러 등록 사용예정.
 * TODO. @EnableScheduling. Ex) Remove with 1-Year User Data... 로직 추가등 유용하게 활용 가능.
 * @EnableAspectJAutoProxy 오토프로시
 * @MapperScan 매퍼 스케너 매퍼 단일 등록이 아닌 패키지 전체 등록.
 * */

@SpringBootApplication
@MapperScan(value = {"jelog.server.main"})
@EnableAspectJAutoProxy
@EnableScheduling
public class JelogApplication {

    public static void main(String[] args) {
        SpringApplication.run(JelogApplication.class, args);
    }

}
