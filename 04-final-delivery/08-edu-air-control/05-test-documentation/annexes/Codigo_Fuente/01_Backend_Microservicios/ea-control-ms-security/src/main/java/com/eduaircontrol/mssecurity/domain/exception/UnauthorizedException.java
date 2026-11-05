package com.eduaircontrol.mssecurity.domain.exception;

public class UnauthorizedException extends RuntimeException {

    private final String code;

    public UnauthorizedException(String message) {
        this("INVALID_CREDENTIALS", message);
    }

    public UnauthorizedException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
