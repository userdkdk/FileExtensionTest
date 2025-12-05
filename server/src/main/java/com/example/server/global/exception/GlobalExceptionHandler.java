package com.example.server.global.exception;

import com.example.server.global.common.response.CommonResponse;
import com.example.server.global.exception.code.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.NoSuchAlgorithmException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<CommonResponse<Object>> handleCustomException(CustomException e, HttpServletRequest request) {
        if (!e.getParameters().isEmpty()) {
            return CommonResponse.error(e.getErrorCode(), e.getParameters());
        }
        return CommonResponse.error(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<CommonResponse<Object>> handleMethodArgumentNotValidExceptionException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        FieldError fieldError = e.getBindingResult().getFieldError();

        if (fieldError == null) {
            log.error("[ValidationException] {} {}: 유효성 검증 에러",
                    request.getMethod(),
                    request.getRequestURI());
            return CommonResponse.error(ErrorCode.INVALID_INPUT_VALUE);
        }

        String field = fieldError.getField();
        String defaultMessage = fieldError.getDefaultMessage();
        Object rejectedValue = fieldError.getRejectedValue();

        log.error("[ValidationException] {} {}: field '{}' - {} (입력값: '{}')",
                request.getMethod(),
                request.getRequestURI(),
                field,
                defaultMessage,
                rejectedValue
        );
        return CommonResponse.error(ErrorCode.INVALID_INPUT_VALUE,
                rejectedValue+", "+defaultMessage);
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    protected ResponseEntity<CommonResponse<Object>> handleNoSuchAlgorithmException(
            NoSuchAlgorithmException e, HttpServletRequest request) {
        return CommonResponse.error(ErrorCode.INVALID_INPUT_VALUE);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonResponse<Object>> handleException(
            Exception e, HttpServletRequest request) {
        return CommonResponse.error(ErrorCode.INTERNAL_SERVER_ERROR,e.getMessage());
    }
}
