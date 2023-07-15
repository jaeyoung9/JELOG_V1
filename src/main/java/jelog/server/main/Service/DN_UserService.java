package jelog.server.main.Service;


import jelog.server.main.Model.DN_UserModel;
import jelog.server.main.Repositories.DN_UserRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    /**
     * [User]
     * Add
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    public DN_UserModel createUser(final DN_UserModel entity){

        // Data Check
        validateUser(entity);

        dn_userRepositories.save(entity);
        log.info("Use : {} is saved.", entity.getDnUid());
        return dn_userRepositories.findById(entity.getDnUid()).get();
    }

    /**
     * [User]
     * Sign-In
     * */
    public ResponseEntity<?> signUser(){
        return null;
    }



}
