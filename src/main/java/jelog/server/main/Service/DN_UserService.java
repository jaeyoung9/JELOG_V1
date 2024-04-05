package jelog.server.main.Service;


import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Global.Encrypt;
import jelog.server.main.Model.DN_UserModel;
import jelog.server.main.Model.Jwt.Authority;
import jelog.server.main.Repositories.DN_UserRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static jelog.server.main.Controller.BaseController.randomLetters;

/**
 * [Service]
 * DN_Users
 * */
//-----------------------------------------------------------------------------------------------------------------------------------------
@Slf4j
@Service
public class DN_UserService {

    /**
     * [Variables]
     * Repository
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    @Autowired
    private DN_UserRepositories dn_userRepositories;

    /**
     * [User]
     * Validate
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    public void validateUser(final DN_UserModel _model){
        if(null == _model){
            log.warn("data is check for cannot be null.");
            throw new RuntimeException("data is check for cannot be null.");
        }
    }

    public void validateUserId(final DN_UserModel _model){
        boolean data = dn_userRepositories.existsByDaSignID(_model.getDaSignID());
        if(false == data){
            log.warn("sign data is check for cannot be null");
            throw new RuntimeException("sign data is check for cannot be null");
        }
    }

    public void validateExistUserId(final DN_UserModel _model){
        boolean data = dn_userRepositories.existsByDaSignID(_model.getDaSignID());
        if(true == data){
            log.warn("exist User SignId");
            throw  new RuntimeException("exist User SignId");
        }
    }

    public void validateSign(final String a, final String b){

        if(null != a & null != b){
            Optional<DN_UserModel> u = dn_userRepositories.findByDaSignID(a);
            String salt = u.get().getDnSalt();

            if(!Encrypt.getEncrypt(b, salt).equals(u.get().getDnPasswd())){
                log.warn("exist password does not match.");
                throw  new RuntimeException("exist password does not match.");
            }

        }else{
            log.warn("!exist data");
            throw  new RuntimeException("!exist data");
        }

    }

    /**
     * [User]
     * Issuing a user account
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    @Transactional
    public DN_UserModel createUser(final DN_UserModel entity){

        try{
            validateUser(entity);
            validateExistUserId(entity);

            if(null == entity.getDnName()){ entity.setDnName(randomLetters(8)); }

            // Salt, SHA-256 Password Add
            entity.setDnSalt(Encrypt.getSalt());
            entity.setDnPasswd(Encrypt.getEncrypt(entity.getDnPasswd(), entity.getDnSalt()));

            // 권한 설정
            assignRole(entity);

            // 사용자 생성
            DN_UserModel savedEntity = dn_userRepositories.save(entity);
            log.info("User: {} is saved.", savedEntity.getDnUid());

            return savedEntity;
        }catch(Exception e){
            log.error("Error creating user: {}", e.getMessage(), e);
            throw new RuntimeException("User creation failed.");
        }
    }

    /**
     * [User]
     * Issuing a user account
     * 권한설정
     * */
    private void assignRole(DN_UserModel entity) {
        String roleName;
        switch (entity.getDnUserAuthEnum().getTitleCode()) {
            case 24678:
                roleName = "ROLE_ADMIN";
                break;
            case 20000:
            case 20344:
            default:
                roleName = "ROLE_USER";
                break;
        }

        Authority authority = Authority.builder().name(roleName).build();
        authority.setDnUserModel(entity);
        entity.setRoles(Collections.singletonList(authority));
    }

    /**
     * [User]
     * Sign-In
     * */
    public DN_UserModel signUser(final String signUserID, final String dnPassword){
        try {
            validateSign(signUserID, dnPassword);
            Optional<DN_UserModel> user = dn_userRepositories.findByDaSignID(signUserID);
            String authPassword = Encrypt.getEncrypt(dnPassword, user.get().getDnSalt());
            DN_UserModel authUser = dn_userRepositories.findByDaSignIDAndDnPasswd(signUserID, authPassword);
            validateUser(authUser);
            validateUserId(authUser);

            // Password Changing
            authUser.setDnPasswd("*****");
            authUser.setDnSalt("*****");
            return authUser;
        }catch (Exception e){
            log.error("Error signing in user: {}", e.getMessage(), e);
            throw new RuntimeException("User sign-in failed. ", e);
        }
    }

}
