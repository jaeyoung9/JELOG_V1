package jelog.server.main.Controller.SNS1012;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Dto.DT_Content;
import jelog.server.main.Global.ResponseDTO;
import jelog.server.main.Model.DN_Content;
import jelog.server.main.Service.DN_ContentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Description :
 * PackageName : jelog.server.main.Controller.SNS1012
 * FileName : SNS1012Controller
 * Author : User
 * TimeDate : 2023-10-16
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-10-16               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@RestController
@RequestMapping(value = "/api/auth")
public class SNS1012Controller extends BaseController {
    /**
     * [Variables]
     * DN_UserService
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    private DN_ContentService contentService;
    public SNS1012Controller(DN_ContentService _contentService){
        this.contentService = _contentService;
    }



    /**
     * [writingOut]
     * contentWritingOut New
     *
     * */
    @PostMapping(value = "/cwo/action")
    public ResponseEntity<?> contentWriting(@RequestBody DT_Content dto){


        Map<String, Object> map = new HashMap<>();

        ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
