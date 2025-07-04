package jelog.server.main.Global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;

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
    @ExceptionHandler({Exception.class})
    public ModelAndView handlerAll(Exception e){
        log.error("Unhandled exception occurred", e);
        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("message", "서버 오류가 발생했습니다.");
        return modelAndView;
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ModelAndView handleUsernameNotFoundException(UsernameNotFoundException e){
        log.error("Invalid authentication!", e);
        ModelAndView modelAndView = new ModelAndView("error/404");
        modelAndView.addObject("message", "사용자를 찾을 수 없습니다.");
        return modelAndView;
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ModelAndView handleAccessDeniedException(AccessDeniedException e) {
        log.error("Access denied!", e);
        ModelAndView modelAndView = new ModelAndView("error/403");
        modelAndView.addObject("message", "접근 권한이 없습니다.");
        return modelAndView;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ResponseDTO> handleValidationExceptions(Exception e) {
        log.error("Validation error!", e);
        return ResponseEntity.badRequest().body(
            ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("입력 값이 올바르지 않습니다.")
                .build()
        );
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ResponseDTO> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("Method not supported!", e);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
            ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("지원하지 않는 HTTP 메서드입니다.")
                .build()
        );
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("Data integrity violation!", e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ResponseDTO.builder()
                .result(ResponseResult.FAIL)
                .message("데이터 무결성 오류가 발생했습니다.")
                .build()
        );
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Entity not found!", e);
        ModelAndView modelAndView = new ModelAndView("error/404");
        modelAndView.addObject("message", "요청한 리소스를 찾을 수 없습니다.");
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({NullPointerException.class})
    public void handleNullPointerException(NullPointerException e) {
        log.error("Null pointer exception occurred!", e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public void handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Bad arguments provided!", e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({EntityNotFoundException.class})
    public void handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Entity not found!", e);
    }

}
