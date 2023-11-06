package jelog.server.main.Controller.SNS1012;

import jelog.server.main.Controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Description :
 * PackageName : jelog.server.main.Controller.SNS1012
 * FileName : SNS1012ViewController
 * Author : User
 * TimeDate : 2023-10-16
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-10-16               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@RestController
@RequestMapping(value = "/api/view/auth")
public class SNS1012ViewController extends BaseController {

    /**
     * [writingOut]
     * contentWritingOut
     *
     * */
    @GetMapping("/cwo/new")
    public ModelAndView showWritingPage() {
        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("data", "page/writingOut");
        return modelAndView;
    }

}
