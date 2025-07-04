package jelog.server.main.Controller.ADM1011;

import jelog.server.main.Controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Description :
 * PackageName : jelog.server.main.Controller.ADM1011
 * FileName : ADM1011ViewController
 * Author : User
 * TimeDate : 2024-04-04
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2024-04-04               jyMin                       최초생성
 * ------------------------------------------------------------
 */

@RestController
@RequestMapping(value = "/api/view/republic")
public class ADM1011ViewController extends BaseController {

    @GetMapping("/mains")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("data", "authPage/main");
        return modelAndView;
    }

}
