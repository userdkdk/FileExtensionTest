package com.example.server.global.exception;

import com.example.server.global.common.response.CommonResponse;
import com.example.server.global.exception.code.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.NoSuchAlgorithmException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<CommonResponse<Object>> handleCustomException(CustomException e, HttpServletRequest request) {
        if (!e.getParameters().isEmpty()) {
            return CommonResponse.error(e.getErrorCode(), e.getParameters());
        }
        return CommonResponse.error(e.getErrorCode());
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    protected ResponseEntity<CommonResponse<Object>> handleNoSuchAlgorithmException(Exception e, HttpServletRequest request) {
        return CommonResponse.error(ErrorCode.INVALID_INPUT_VALUE);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonResponse<Object>> handleException(Exception e, HttpServletRequest request) {
        return CommonResponse.error(ErrorCode.INTERNAL_SERVER_ERROR,e.getMessage());
    }
}
