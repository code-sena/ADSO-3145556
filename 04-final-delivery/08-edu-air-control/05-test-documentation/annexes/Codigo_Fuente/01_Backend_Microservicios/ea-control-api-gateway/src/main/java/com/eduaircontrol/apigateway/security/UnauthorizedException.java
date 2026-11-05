package com.eduaircontrol.apigateway.security;

/**
 * Error de autenticación en el borde. El {@code code} viaja en el cuerpo JSON
 * de la respuesta 401 para que el cliente pueda distinguir el motivo.
 */
public class UnauthorizedException extends RuntimeException {

    private final String code;

    public UnauthorizedException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
