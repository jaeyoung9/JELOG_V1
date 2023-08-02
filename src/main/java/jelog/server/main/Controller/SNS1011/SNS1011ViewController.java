package jelog.server.main.Controller.SNS1011;

import jelog.server.main.Controller.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

/**
 * Description :
 * PackageName : jelog.server.main.Controller.SNS1011
 * FileName : SNS1011ViewController
 * Author : MinJaeYoung
 * TimeDate : 2023-08-02
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-08-02               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@RestController
@RequestMapping(value = "/api/view/public")
public class SNS1011ViewController extends BaseController {
    @GetMapping("/mains")
    public ModelAndView showMainPage() {
        return new ModelAndView("main"); // View name;
    }
}
