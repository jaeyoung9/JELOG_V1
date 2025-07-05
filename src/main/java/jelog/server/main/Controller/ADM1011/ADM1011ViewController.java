package jelog.server.main.Controller.ADM1011;

import jelog.server.main.Controller.BaseController;
import jelog.server.main.Service.DN_ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
@PreAuthorize("hasRole('ADMIN')")
public class ADM1011ViewController extends BaseController {

    private final DN_ContentService contentService;

    @Autowired
    public ADM1011ViewController(DN_ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/mains")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("data", "authPage/main");
        return modelAndView;
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("data", "admin/dashboard");
        return modelAndView;
    }

    @GetMapping("/posts")
    public ModelAndView managePosts() {
        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("data", "admin/post-list");
        return modelAndView;
    }

    @GetMapping("/posts/new")
    public ModelAndView createPost() {
        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("data", "admin/post-editor");
        return modelAndView;
    }

    @GetMapping("/posts/edit/{postId}")
    public ModelAndView editPost(@PathVariable int postId) {
        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("data", "admin/post-editor");
        
        try {
            var post = contentService.findContent(postId);
            if (post != null) {
                modelAndView.addObject("post", post);
            }
        } catch (Exception e) {
            // Handle error appropriately
        }
        
        return modelAndView;
    }

    @GetMapping("/users")
    public ModelAndView manageUsers() {
        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("data", "admin/user-list");
        return modelAndView;
    }

    @GetMapping("/settings")
    public ModelAndView settings() {
        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("data", "admin/settings");
        return modelAndView;
    }

    @GetMapping("/comments")
    public ModelAndView manageComments() {
        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("data", "admin/comment-list");
        return modelAndView;
    }

    @GetMapping("/categories")
    public ModelAndView manageCategories() {
        ModelAndView modelAndView = new ModelAndView("fragments/layout");
        modelAndView.addObject("data", "admin/category-management");
        return modelAndView;
    }

}
