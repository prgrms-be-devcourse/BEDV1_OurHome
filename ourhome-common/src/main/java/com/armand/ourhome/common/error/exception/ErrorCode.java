package com.armand.ourhome.common.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input value"),
    INVALID_TYPE_VALUE(400, "C002", " Invalid Type Value"),
    ENTITY_NOT_FOUND(400, "C003", "Entity Not Found"),
    METHOD_NOT_ALLOWED(405, "C004", "Invalid Input value"),
    INTERNAL_SERVER_ERROR(500, "C005", "Server Error"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is denied");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}