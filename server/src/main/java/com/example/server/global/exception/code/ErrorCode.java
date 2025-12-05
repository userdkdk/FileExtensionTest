package com.example.server.global.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C-001","server error"),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C-002", "Invalid input error"),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "C-003", "Unauthorized user access"),

    // file extension


    SAMPLE(HttpStatus.UNAUTHORIZED, "S-003", "Unauthorized user access");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
