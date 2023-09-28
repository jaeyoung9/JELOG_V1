package jelog.server.main.Controller.SNS1011;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Global.ResponseDTO;
import jelog.server.main.Model.DN_Content;
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
import java.util.Optional;

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
 * 2023-08-02               MinJaeYoung                내용추가
 * ------------------------------------------------------------
 * 2023-09-28               MinJaeYoung                상세페이지 테스트
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

    /**
     * [Main]
     * Main Detail Page
     *
     * RestApi Test Data
     *     "result": {
     *         "code": 200,
     *         "message": "Success",
     *         "response_time": "2023-09-28T05:12:25.013+00:00",
     *         "request_action": null
     *     },
     *     "payload": {
     *         "data": {
     *             "contentId": 987699,
     *             "contentCategories": "Other",
     *             "contentThumbnail": "https://t1.daumcdn.net/cfile/tistory/24283C3858F778CA2E",
     *             "contentTitle": "이것은 제목입니다.",
     *             "contentBody": "이것은 내용입니다.이것은 내용입니다.이것은 내용입니다.",
     *             "views": 2,
     *             "inDate": null,
     *             "upDate": null
     *         }
     *     }
     * 1. 페이지 접근시 조회수 카운팅 필요.
     * 2. 작성일 자동등록 안되는 내용 확인 필요.
     * 2-1. 현재 DN_Content 모델에 등록된 어노텐션 @CreationTimestamp 관련 내용 수집필요.
     * */
    @GetMapping(value = "/mains/{listNumber}/")
    public ResponseEntity<?> mainPage(@PathVariable int listNumber){

        // get data through number
        Optional<DN_Content> getContent = dnContent.findContent(listNumber);

        Map<String, Object> map = new HashMap<>();
        map.put("data", getContent);
        ResponseDTO responseDTO = ResponseDTO.builder().payload(map).build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
