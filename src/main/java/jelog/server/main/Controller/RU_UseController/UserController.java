package jelog.server.main.Controller.RU_UseController;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Dto.DT_UserDto;
import jelog.server.main.Global.GlobalExceptionHandler;
import jelog.server.main.Global.ResponseDTO;
import jelog.server.main.Model.DN_UserModel;
import jelog.server.main.Service.DN_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


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

    public UserController(DN_UserService _userSerivce){
        this.userService = _userSerivce;
    }


    /**
     * [User]
     * User in Sign-In
     * */
    @PostMapping(value = "/ko-jy/in/sign/")
    public ResponseEntity<?> signUser(HttpServletRequest request) {

        String signUserID = request.getParameter("daSignID");
        DN_UserModel entity = userService.signUser(signUserID);

        if(null == entity) return null;
        else if(entity.getDnPasswd().equals(request.getParameter("dnPassword"))){

            Map<String ,Object> map = new HashMap<>();
            map.put("JY-ACCESS-TOKEN","");
            map.put("JY-REFRESH-TOKEN","");
            ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
            return ResponseEntity.ok().body(responseDTO);
        }
        return null;
    }

    /**
     * [User]
     * User in Sign-Up
     * */
    @PostMapping(value = "/ko-jy/up/sign/")
    public ResponseEntity<?> signupInfoUser(@RequestBody DT_UserDto dto){
        DN_UserModel entity = userService.createUser(DT_UserDto.dnUserEntity(dto));

        return null;
    }
}
