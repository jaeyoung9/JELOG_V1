package jelog.server.main.Controller.SNS1011;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Global.ResponseDTO;
import jelog.server.main.Service.DN_ContentService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * [Variables]
     * DN_ContentService
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    private DN_ContentService dnContent;
    public SNS1011Controller(DN_ContentService _dnContent){
        this.dnContent = _dnContent;
    }


    /**
     * [Main]
     * Main Page Result Data
     * */
    @GetMapping(value = "/mains/")
    public ResponseEntity<?> mains(@PageableDefault(size = 10) Pageable pageable, String title){

        Map<String, Object> map = new HashMap<>();
        map.put("data",dnContent.findPage(pageable, title));
        ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
