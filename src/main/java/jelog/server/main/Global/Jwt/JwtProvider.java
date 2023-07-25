//package jelog.server.main.Global.Jwt;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import jelog.server.main.Dto.DT_UserDto;
//import jelog.server.main.Model.DN_UserModel;
//import jelog.server.main.Service.DN_UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.servlet.http.HttpServletRequest;
//import java.util.Base64;
//import java.util.Date;
//import java.util.List;
//
///**
// * Description :
// * PackageName : jelog.server.main.Global.Jwt
// * FileName : JwtProvider
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
//@RequiredArgsConstructor
//public class JwtProvider {
//
//    /**
//     * [Variables]
//     * */
//    //-----------------------------------------------------------------------------------------------------------------------------------------
//    private String secretKey = "osJwt";
//    private Long validTokenTime = 30 * 60 * 60 * 1000L;
//    private final DN_UserService dnUserService;
//
//    /**
//     * [protected]
//     * PostConstruct
//     * 의존 주입
//     * */
//    //-----------------------------------------------------------------------------------------------------------------------------------------
//    @PostConstruct
//    protected void init(){
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//    }
//
//
//    public String createToken(DT_UserDto dto) {
//
//        Claims claims = Jwts.claims().setSubject(dto.getDaSignID());
//        claims.put("roles", dto);
//        Date now = new Date();
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(new Date(now.getTime() + validTokenTime))
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//    }
//
//
//    public Authentication getAuthentication(String token) {
//        UserDetails userDetails = dnUserService.validateJwt(this.getUserPk(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "",userDetails.getAuthorities());
//    }
//
//    public String getUserPk(String token) {
//        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
//    }
//
//    public String resolveToken(HttpServletRequest request) {
//        return request.getHeader("JY-ACCESS-TOKEN");
//    }
//
//    public boolean validateToken(String jwtToken) {
//        try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
//            return !claims.getBody().getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//}
