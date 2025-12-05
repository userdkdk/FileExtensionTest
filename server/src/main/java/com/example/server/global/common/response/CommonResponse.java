package com.example.server.global.common.response;

import com.example.server.global.exception.code.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
public class CommonResponse<T> {

    private final boolean success;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final ErrorResponse error;
    private final LocalDateTime timestamp;

    private CommonResponse(boolean success, T data, ErrorResponse error) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }

    /**
     *  HTTP 200 OK, NO DATA
     * @return API 응답을포함하는 ResponseEntity
     * @param <T> 응답 데이터의 타입
     */
    public static <T> ResponseEntity<CommonResponse<T>> ok() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonResponse<>(true, null, null));
    }

    public static <T> ResponseEntity<CommonResponse<T>> ok(T data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonResponse<>(true, data, null));
    }

    public static <T> ResponseEntity<CommonResponse<T>> ok(ResponseCookie... cookies) {
        HttpHeaders headers = new HttpHeaders();
        for (ResponseCookie cookie : cookies) {
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(new CommonResponse<>(true, null, null));
    }

    public static <T> ResponseEntity<CommonResponse<T>> create() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonResponse<>(true, null, null));
    }

    public static <T> ResponseEntity<CommonResponse<T>> error(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new CommonResponse<>(false, null, new ErrorResponse(errorCode.getCode(),errorCode.getMessage())));
    }

    public static <T> ResponseEntity<CommonResponse<T>> error(ErrorCode errorCode, String message) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new CommonResponse<>(false, null, new ErrorResponse(errorCode.getCode(),message)));
    }

    public static <T> ResponseEntity<CommonResponse<T>> error(ErrorCode errorCode, T data) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new CommonResponse<>(false, data, new ErrorResponse(errorCode.getCode(),errorCode.getMessage())));
    }
}
