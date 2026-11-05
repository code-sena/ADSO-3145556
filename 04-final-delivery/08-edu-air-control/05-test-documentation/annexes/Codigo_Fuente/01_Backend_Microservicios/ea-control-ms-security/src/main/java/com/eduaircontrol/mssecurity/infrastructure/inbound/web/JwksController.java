package com.eduaircontrol.mssecurity.infrastructure.inbound.web;

import com.eduaircontrol.mssecurity.infrastructure.security.JwtService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** JWKS (RFC 7517): clave pública para validar tokens RS256 sin llamar al servicio. */
@RestController
@RequiredArgsConstructor
public class JwksController {

    private final JwtService jwtService;

    @GetMapping("/api/v1/auth/jwks")
    public Map<String, Object> jwks() {
        return jwtService.jwks();
    }
}
