package jelog.server.main.Controller.ADM1011;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Global.Jwt.JwtProvider;
import jelog.server.main.Global.ResponseDTO;
import jelog.server.main.Model.DN_Content;
import jelog.server.main.Service.DN_LogService;
import jelog.server.main.Service.DN_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description :
 * PackageName : jelog.server.main.Controller.ADM1011
 * FileName : ADM1011Controller
 * Author : User
 * TimeDate : 2024-04-04
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2024-04-04               User                최초생성
 * ------------------------------------------------------------
 */

@RestController
@RequestMapping(value = "/api/republic")
public class ADM1011Controller extends BaseController {


    /**
     * [Variables]
     * DN_UserService
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    private final DN_UserService userService;
    private final DN_LogService logService;

    @Autowired
    public ADM1011Controller(DN_UserService _userService , DN_LogService _logService){
        this.userService = _userService; this.logService = _logService;
    }

    /**
     * [Main]
     * Main Dash Board
     *
     * */
    @GetMapping(value = "/dash-board")
    public ResponseEntity<?> mainDashBoard() {


        Map<String, Object> map = new HashMap<>();
        map.put("data", "data");
        ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
