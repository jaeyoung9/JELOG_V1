//package jelog.server.main.Global;
//
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
///**
// * Description :
// * PackageName : jelog.server.main.Global
// * FileName : CustomAccessDeniedHandler
// * Author : MinJaeYoung
// * TimeDate : 2023-07-24
// * ============================================================
// * DATE                      AUTHOR                      NOTE
// * ------------------------------------------------------------
// * 2023-07-24               MinJaeYoung                최초생성
// * ------------------------------------------------------------
// */
//
//@Component
//public class CustomAccessDeniedHandler implements AccessDeniedHandler {
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        response.sendRedirect("/error");
//    }
//}
