package jelog.server.main.Config;

import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Global.CommonUtils;
import jelog.server.main.Model.DN_LogModel;
import jelog.server.main.Repositories.DN_LogRepositories;
import jelog.server.main.Repositories.DN_UserRepositories;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Description :
 * PackageName : jelog.server.main.Config
 * FileName : LoggingAspect
 * Author : User
 * TimeDate : 2024-04-08
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2024-04-08               User                최초생성
 * ------------------------------------------------------------
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * [Variables]
     * Repository
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    private final DN_LogRepositories logRepositories;
    @Autowired
    public LoggingAspect(DN_LogRepositories logRepositories){
        this.logRepositories = logRepositories;
    }

    @Pointcut("execution(* jelog.server.main.Controller..*(..))")
    public void controllerLayer() {
        // 컨트롤러 레이어의 모든 메소드를 대상으로 포인트컷 정의
    }

    @Before("controllerLayer()")
    public void logBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String requestURI = request.getRequestURI();

            // URI 검증
            if (!requestURI.startsWith("/api/view/")) {
                // 사용자 OS
                String userAgent = request.getHeader("User-Agent");
                OsEnum os = CommonUtils.detectOS(userAgent);

                // IP 출력
                String ipAddress = getClientIpAddress(request);


                DN_LogModel log = DN_LogModel.builder()
                        .dseIP(ipAddress)
                        .dseSignID(request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous")
                        .queryString(requestURI)
                        .dnOs(os)
                        .build();

                logRepositories.save(log);
            }
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Real-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0]; // X-Forwarded-For 헤더에 여러 주소가 있을 경우 첫 번째 주소 사용
        }
        return ipAddress;
    }

}
