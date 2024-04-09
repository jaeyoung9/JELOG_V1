package jelog.server.main.Controller.RU_UseController;

import jelog.server.main.Controller.BaseController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Description :
 * PackageName : jelog.server.main.Controller.RU_UseController
 * FileName : UserViewController
 * Author : MinJaeYoung
 * TimeDate : 2023-08-10
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-08-10               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@RequestMapping(value = "/api/view/public")
@RestController
public class UserViewController extends BaseController {

    /**
     * [Sign]
     * Sing In
     * */
    @GetMapping("/in/sign/")
    public ModelAndView showMainPage() {
        // 로그인 페이지 리다이렉트.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || "anonymousUser".equals(authentication.getPrincipal().toString())) {
            ModelAndView modelAndView = new ModelAndView("fragments/layout");
            modelAndView.addObject("data", "page/inSign");
            return modelAndView;
        }else {
            return new ModelAndView("redirect:/");
        }

    }

}
