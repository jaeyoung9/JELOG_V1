package jelog.server.main.Controller.SNS1012;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Dto.DT_Content;
import jelog.server.main.Global.ResponseDTO;
import jelog.server.main.Model.DN_Content;
import jelog.server.main.Service.DN_ContentService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final DN_ContentService contentService;

    @Autowired
    public SNS1012Controller(DN_ContentService _contentService){
        this.contentService = _contentService;
    }



    /**
     * [writingOut]
     * contentWritingOut New
     * @return Json Data
     * */
    @PostMapping(value = "/cwo/action")
    public ResponseEntity<?> contentWriting(@RequestBody DT_Content dto, HttpServletRequest request){
        try {
            // Handle files safely
            if (dto.getFiles() != null && dto.getFiles().size() != 0) {
                dto.setContentThumbnail(dto.getFiles().get(0).getFilePath());
            }

            // Set a default author if not provided
            if (dto.getAuthor() == null || dto.getAuthor().trim().isEmpty()) {
                dto.setAuthor("작성자");
            }

            DN_Content checkContent = contentService.createContent(dto);
            Map<String, Object> map = new HashMap<>();
            map.put("data", checkContent);
            ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            e.printStackTrace(); // Add logging for debugging
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Content creation failed: " + e.getMessage());
            ResponseDTO responseDTO = ResponseDTO.builder()
                .result(jelog.server.main.Global.ResponseResult.FAIL)
                .message("게시물 작성 중 오류가 발생했습니다.")
                .payload(errorMap)
                .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
