package jelog.server.main.Global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public void handlerAll(Exception e){
        log.error("Unhandled exception occurred", e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UsernameNotFoundException.class})
    public void UsernameNotFoundException(UsernameNotFoundException e){
        log.error("Invalid authentication!", e);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public void handleAccessDeniedException(AccessDeniedException e) {
        log.error("Access denied!", e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public void handleValidationExceptions(Exception e) {
        log.error("Validation error!", e);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public void handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("Method not supported!", e);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({DataIntegrityViolationException.class})
    public void handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("Data integrity violation!", e);
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
