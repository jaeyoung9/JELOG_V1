package jelog.server.main.Controller.RU_UseController;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Dto.DT_UserDto;
import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Global.Encrypt;
import jelog.server.main.Global.Jwt.JwtProvider;
import jelog.server.main.Global.ResponseDTO;
import jelog.server.main.Model.DN_UserModel;
import jelog.server.main.Service.DN_UserService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

@RequestMapping(value = "/api")
@RestController
public class UserController extends BaseController {

    /**
     * [Variables]
     * DN_UserService
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    private final DN_UserService userService;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserController(DN_UserService _userService, JwtProvider jwtProvider){
        this.userService = _userService; this.jwtProvider = jwtProvider;
    }


    /**
     * [User]
     * User in Sign-In
     * */
    @PostMapping(value = "/ko-jy/in/sign/")
    public ResponseEntity<?> signUser(@RequestBody DT_UserDto dto) {
        DN_UserModel entity = userService.signUser(dto.getDaSignID(), dto.getDnPasswd());
        Map<String, Object> map = new HashMap<>();
        if(entity.getDnUserAuthEnum() != OsEnum.OP_User0) {
            map.put("JY-ACCESS-TOKEN", jwtProvider.createToken(entity.getDaSignID(), entity.getRoles()));
        }else{
            map.put("JY-ACCESS-TOKEN", jwtProvider.createToken(entity.getDaSignID(), null));
        }
        map.put("JY-REFRESH-TOKEN", "*****" + Encrypt.getSalt());           // TODO : REFRESH TOKEN 적용 필요.
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
        userService.createUser(DT_UserDto.dnUserEntity(dto));
        return signUser(dto);
    }

}
