package jelog.server.main.Controller.RU_UseController;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Dto.DT_UserDto;
import jelog.server.main.Global.GlobalExceptionHandler;
import jelog.server.main.Global.ResponseDTO;
import jelog.server.main.Model.DN_UserModel;
import jelog.server.main.Service.DN_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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

        DN_UserModel entity = userService.signUser(request.getParameter("daSignID"), request.getParameter("daSignID"));

        Map<String, Object> map = new HashMap<>();
        map.put("JY-ACCESS-TOKEN", "");
        map.put("JY-REFRESH-TOKEN", "");

        ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
        return ResponseEntity.ok().body(responseDTO);
    }

    /**
     * [User]
     * User in Sign-Up
     * automatic Sign-In after Sign-Up
     * */
    @PostMapping(value = "/ko-jy/up/sign/")
    public ResponseEntity<?> signupInfoUser(@RequestBody DT_UserDto dto){

        DN_UserModel entity = userService.createUser(DT_UserDto.dnUserEntity(dto));

        Map<String,Object> map = new HashMap<>();
        map.put("daSignID", entity.getDaSignID());
        map.put("dnPassword", entity.getDnPasswd());

        return signUser(serverServletRequest(map));
    }

    /**
     * [User]
     * 삭제 예정 테스트 read
     * */
    @GetMapping(value = "/ko-get/{signUserID}/")
    public ResponseEntity<?> userInfo(@PathVariable("signUserID") String signUserID){
        Map<String ,Object> map = new HashMap<>();
        DN_UserModel entity = userService.signUser(signUserID, "");
        map.put("data", entity);
        ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
