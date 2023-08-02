package jelog.server.main.Controller.SNS1011;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Global.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Description :
 * PackageName : jelog.server.main.Controller.SNS1011
 * FileName : SNS1011Controller
 * Author : MinJaeYoung
 * TimeDate : 2023-07-28
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-28               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 * 2023-08-02               MinJaeYoung
 */
@RestController
@RequestMapping(value = "/api/public")
public class SNS1011Controller extends BaseController {

    @GetMapping(value = "/mains/")
    public ResponseEntity<?> mains(){
        Map<String, Object> map = new HashMap<>();
        map.put("test","data");
        map.put("test1","data");
        map.put("test2","data");
        ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
