package jelog.server.main.Global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Description :
 * PackageName : jelog.server.main.Global
 * FileName : GlobalExceptionHandler
 * Author : MinJaeYoung
 * TimeDate : 2023-07-15
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-15               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * [Handler]
     * Variables
     * */
    //---------------------------------------------------------------------------------------------------
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * [Handler]
     * handlerAll
     * */
    //---------------------------------------------------------------------------------------------------
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public void handlerAll(Exception e){
        log.error("Unhandled exception occurred", e);
    }

}
