package jelog.server.main.Service.Jwt;

import jelog.server.main.Model.DN_UserModel;
import jelog.server.main.Model.Jwt.CustomUserDetails;
import jelog.server.main.Repositories.DN_UserRepositories;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Description :
 * PackageName : jelog.server.main.Service.Jwt
 * FileName : JpaUserDetailsService
 * Author : MinJaeYoung
 * TimeDate : 2023-07-26
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-26               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final DN_UserRepositories dn_userRepositories;

    @Override
    public UserDetails loadUserByUsername(String signUserID) throws UsernameNotFoundException {
        DN_UserModel entity = dn_userRepositories.findByDaSignID(signUserID).orElseThrow(
                () -> new UsernameNotFoundException("Invalid authentication!")
        );

        return new CustomUserDetails(entity);
    }
}
