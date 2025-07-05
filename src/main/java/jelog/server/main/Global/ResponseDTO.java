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
    public String result = "SUCCESS";
    public T payload;
    public String message;

    public ResponseDTO(String result, String message, T _payload){
        this.result = result;
        this.message = message;
        this.payload = _payload;
    }
    
    // Static helper methods for common responses
    public static <T> ResponseDTO<T> success(T payload) {
        return ResponseDTO.<T>builder()
            .result(ResponseResult.SUCCESS)
            .payload(payload)
            .build();
    }
    
    public static <T> ResponseDTO<T> fail(String message) {
        return ResponseDTO.<T>builder()
            .result(ResponseResult.FAIL)
            .message(message)
            .build();
    }
    
    public static <T> ResponseDTO<T> error(String message) {
        return ResponseDTO.<T>builder()
            .result(ResponseResult.ERROR)
            .message(message)
            .build();
    }
}
