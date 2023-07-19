package jelog.server.main.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Description :
 * PackageName : jelog.server.main.Controller
 * FileName : BaseController
 * Author : MinJaeYoung
 * TimeDate : 2023-07-15
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-15               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@RestController
@RequestMapping(value = "/api")
public class BaseController {

    public HttpServletRequest serverServletRequest(Map<String, Object> map){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        for(Map.Entry<String, Object> a : map.entrySet()){
            request.setAttribute(a.getKey(),a.getValue());
        }

        return request;
    }

}
