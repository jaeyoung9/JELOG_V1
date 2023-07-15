package jelog.server.main.Controller.RU_UseController;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Global.GlobalExceptionHandler;
import jelog.server.main.Service.DN_UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Description :
 * PackageName : jelog.server.main.Controller.RU_UseController
 * FileName : UserController
 * Author : MinJaeYoung
 * TimeDate : 2023-07-15
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-15               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@RestController
public class UserController extends BaseController {

    /**
     * [Variables]
     * DN_UserService
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    private DN_UserService userService;


    /**
     * [User]
     * User in Sign-In
     * */
    @PostMapping(value = "/ko-jy/path/sign/")
    public ResponseEntity<?> signUser() {
        return null;
    }
}
