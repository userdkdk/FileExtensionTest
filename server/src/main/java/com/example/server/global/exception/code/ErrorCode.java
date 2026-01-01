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
    INTERNAL_DB_ERROR(HttpStatus.UNAUTHORIZED, "C-003", "db not found error"),

    // file extension,
    INVALID_EXTENSION_VALUE(HttpStatus.BAD_REQUEST, "F-001","확장자 입력값을 확인하십시오"),
    EXCEED_EXTENSION_COUNT(HttpStatus.BAD_REQUEST, "F-002","확장자 200개 초과 입력할 수 없습니다."),
    ERROR_EXTENSION_COUNT(HttpStatus.BAD_REQUEST, "F-005","해당 확장자를 삭제할 수 없습니다."),
    EXTENSION_NOT_FOUND(HttpStatus.NOT_FOUND, "F-003", "해당 확장자를 찾을 수 없습니다."),
    INVALID_EXTENSION_STATUS(HttpStatus.BAD_REQUEST, "F-004", "이미 체크된 확장자입니다."),

    SAMPLE(HttpStatus.UNAUTHORIZED, "S-003", "Unauthorized user access");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
