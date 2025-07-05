package jelog.server.main.Controller.SNS1011;

import jelog.server.main.Controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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


    /**
     * [Main]
     * Main Page Jsp
     * */
    @GetMapping("/mains")
    public ModelAndView showMainPage() {
        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("data", "page/main");
        return modelAndView;
    }

    /**
     * [Main]
     * Main Page Details
     * */
    @GetMapping("/mains/relay/{listNumber}/")
    public ModelAndView showMainDetail(@PathVariable int listNumber){

        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("contentNumber", listNumber);
        modelAndView.addObject("data", "page/enhanced-details");
        return modelAndView;
    }

    /**
     * [Search]
     * Search Results Page
     * */
    @GetMapping("/search")
    public ModelAndView showSearchResults() {
        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("data", "page/search-results");
        return modelAndView;
    }

}
