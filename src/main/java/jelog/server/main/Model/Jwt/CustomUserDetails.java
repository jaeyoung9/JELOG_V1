package jelog.server.main.Model.Jwt;

import jelog.server.main.Model.DN_UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Description :
 * PackageName : jelog.server.main.Model.Jwt
 * FileName : CustomUserDetails
 * Author : MinJaeYoung
 * TimeDate : 2023-07-26
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-26               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */
public class CustomUserDetails  implements UserDetails {

    private final DN_UserModel dnUserModel;

    public CustomUserDetails(DN_UserModel dnUserModel) {
        this.dnUserModel = dnUserModel;
    }

    public final DN_UserModel getDnUserModel() {
        return dnUserModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return dnUserModel.getRoles().stream().map(o -> new SimpleGrantedAuthority(
                o.getName()
        )).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return dnUserModel.getDnPasswd();
    }

    @Override
    public String getUsername() {
        return dnUserModel.getDaSignID();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
