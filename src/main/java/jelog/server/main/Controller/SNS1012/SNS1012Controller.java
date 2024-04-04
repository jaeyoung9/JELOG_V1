package jelog.server.main.Controller.SNS1012;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Dto.DT_Content;
import jelog.server.main.Global.ResponseDTO;
import jelog.server.main.Model.DN_Content;
import jelog.server.main.Service.DN_ContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> contentWriting(@RequestBody DT_Content dto, HttpServletRequest request){
        try {
            /**
             * 쿠키 체크.
             * Todo : 추후에 사용할까?...
             * */
//            String cookieValue = "";
//            for (Cookie cookie : request.getCookies()) {
//                if(cookie.getName().equals("JY-ACCESS-TOKEN")){
//                    cookieValue = cookie.getValue();
//                }
//            }
//
//            if (null != cookieValue) {
//                boolean tokenBool = quickTokenDecryption(cookieValue);
//                if(tokenBool != true){
//                    Exception e = new Exception("error");
//                    throw e;
//                }
//            }else{
//                Exception e = new Exception("error");
//                throw e;
//            }

            if (dto.getFiles().size() != 0) {
                dto.setContentThumbnail(dto.getFiles().get(0).getFilePath());
            }

            DN_Content checkContent = contentService.createContent(dto);
            Map<String, Object> map = new HashMap<>();
            map.put("data", checkContent);
            ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            return null;
        }
    }
}
