package jelog.server.main.Global.Jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description :
 * PackageName : jelog.server.main.Global.Jwt
 * FileName : JwtAuthenticationFilter
 * Author : MinJaeYoung
 * TimeDate : 2023-07-26
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-26               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilter {
    private final JwtProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Token
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        if(token!=null && jwtTokenProvider.validateToken(token)){
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request,response);
    }
}
