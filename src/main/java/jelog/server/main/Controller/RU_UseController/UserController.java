package jelog.server.main.Controller.RU_UseController;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Dto.DT_UserDto;
import jelog.server.main.Global.Encrypt;
import jelog.server.main.Global.Jwt.JwtProvider;
import jelog.server.main.Global.ResponseDTO;
import jelog.server.main.Model.DN_UserModel;
import jelog.server.main.Service.DN_UserService;
import lombok.Builder;
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
    private DN_UserService userService;
    private JwtProvider jwtProvider;
    public UserController(DN_UserService _userSerivce, JwtProvider jwtProvider){
        this.userService = _userSerivce; this.jwtProvider = jwtProvider;
    }


    /**
     * [User]
     * User in Sign-In
     * */
    @PostMapping(value = "/ko-jy/in/sign/")
    public ResponseEntity<?> signUser(@RequestBody DT_UserDto dto) {
        DN_UserModel entity = userService.signUser(dto.getDaSignID(), dto.getDnPasswd());
        Map<String, Object> map = new HashMap<>();
        map.put("JY-ACCESS-TOKEN", jwtProvider.createToken(entity.getDaSignID(), entity.getRoles()));
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

    /**
     * [User]
     * 삭제 예정 테스트 read
     * 권한 테스트
     * */
    @GetMapping(value = "/ko-get/{signUserID}/")
    public ResponseEntity<?> userInfo(@PathVariable("signUserID") String signUserID){
        Map<String ,Object> map = new HashMap<>();
        DN_UserModel entity = userService.signUser(signUserID, "123445");
        map.put("data", entity);
        ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
        return ResponseEntity.ok().body(responseDTO);
    }
    @GetMapping(value = "/auth/user")
    public String user(){
        return randomLetters(7);
    }

    @GetMapping(value = "/republic")
    public String republic(){
        return "Auth admin!";
    }
}
