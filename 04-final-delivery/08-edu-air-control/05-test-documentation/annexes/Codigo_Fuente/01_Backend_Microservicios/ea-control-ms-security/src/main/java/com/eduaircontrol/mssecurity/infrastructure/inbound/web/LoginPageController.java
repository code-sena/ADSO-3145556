package com.eduaircontrol.mssecurity.infrastructure.inbound.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Página de login del Authorization Server (ADR-017). Incluye el
 * {@code companyCode} para resolver la institución (ADR-016).
 */
@Controller
public class LoginPageController {

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String login() {
        return """
                <!doctype html>
                <html lang="es">
                <head><meta charset="utf-8"><title>EduAirControl — Iniciar sesión</title></head>
                <body>
                  <h1>Iniciar sesión</h1>
                  <form method="post" action="/login">
                    <input name="username" type="email" placeholder="Correo" required>
                    <input name="password" type="password" placeholder="Contraseña" required>
                    <button type="submit">Entrar</button>
                  </form>
                </body>
                </html>
                """;
    }
}
