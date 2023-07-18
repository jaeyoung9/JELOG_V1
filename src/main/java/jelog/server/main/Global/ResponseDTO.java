package jelog.server.main.Global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description :
 * PackageName : jelog.server.main.Global
 * FileName : ResponseDTO
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
public class ResponseDTO<T> {
    @Builder.Default
    public ResponseResult result = new ResponseResult(200, "Success", new Date(), null);
    public T payload;

    public ResponseDTO(int code, String message, String request_action, T _payload){
        result = new ResponseResult();
        result.code = code;
        result.message = message;
        result.request_action = request_action;
        payload = _payload;
    }
}
