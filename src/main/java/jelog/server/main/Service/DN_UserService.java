package jelog.server.main.Service;


import jelog.server.main.Global.Encrypt;
import jelog.server.main.Model.DN_UserModel;
import jelog.server.main.Repositories.DN_UserRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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
     * Add
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    @Transactional
    public DN_UserModel createUser(final DN_UserModel entity){

        validateUser(entity);
        validateExistUserId(entity);

        // Salt, SHA-256 Password Add
        entity.setDnSalt(Encrypt.getSalt());
        entity.setDnPasswd(Encrypt.getEncrypt(entity.getDnPasswd(), entity.getDnSalt()));

        dn_userRepositories.save(entity);

        log.info("Use : {} is saved.", entity.getDnUid());
        return dn_userRepositories.findById(entity.getDnUid()).get();
    }

    /**
     * [User]
     * Sign-In
     * */
    public DN_UserModel signUser(final String signUserID, final String dnPassword){

        validateSign(signUserID, dnPassword);
        Optional<DN_UserModel> user = dn_userRepositories.findByDaSignID(signUserID);
        String authPassword = Encrypt.getEncrypt(dnPassword, user.get().getDnSalt());
        DN_UserModel authUser = dn_userRepositories.findByDaSignIDAndDnPasswd(signUserID,authPassword);
        validateUser(authUser);
        validateUserId(authUser);
        return authUser;
    }





}
