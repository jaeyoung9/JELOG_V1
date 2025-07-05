package jelog.server.main.Global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description :
 * PackageName : jelog.server.main.Global
 * FileName : ResponseResult
 * Author : MinJaeYoung
 * TimeDate : 2023-07-16
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-16               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult {
    public int code;
    public String message;
    public Date response_time;
    public String request_action;
    
    // Constants for common responses
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    public static final String ERROR = "ERROR";
}
