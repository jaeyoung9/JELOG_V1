package jelog.server.main.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Description :
 * PackageName : jelog.server.main.Controller
 * FileName : MainController
 * Author : User
 * TimeDate : 2023-11-04
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-11-04               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */
@Controller
public class MainController {
    @GetMapping("/")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("data", "page/main");
        return modelAndView;
    }

    @GetMapping("/api/pro")
    public ModelAndView index2(){
        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("data", "authPage/authInSign");
        return modelAndView;
    }
}
