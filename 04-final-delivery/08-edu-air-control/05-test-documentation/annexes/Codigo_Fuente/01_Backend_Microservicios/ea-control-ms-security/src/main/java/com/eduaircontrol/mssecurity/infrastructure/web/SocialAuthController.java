package com.eduaircontrol.mssecurity.infrastructure.web;

import com.eduaircontrol.mssecurity.application.AuthResult;
import com.eduaircontrol.mssecurity.application.AuthService;
import com.eduaircontrol.mssecurity.application.SocialAuthService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * Login social (Google, Facebook) via OAuth2 Authorization Code Flow.
 * El frontend redirige aqui, el backend redirige al proveedor, y el callback
 * crea/vincula el usuario y redirige al frontend con la sesion.
 */
@RestController
@RequestMapping("/api/v1/auth/oauth2")
@RequiredArgsConstructor
public class SocialAuthController {

    private final SocialAuthService socialAuthService;
    private final AuthService authService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${google.client-id:}")
    private String googleClientId;

    @Value("${google.client-secret:}")
    private String googleClientSecret;

    @Value("${facebook.app-id:}")
    private String facebookAppId;

    @Value("${facebook.app-secret:}")
    private String facebookAppSecret;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    // ===================== GOOGLE =====================

    @GetMapping("/google")
    public void googleAuth(HttpServletResponse response) throws IOException {
        String redirectUri = getRedirectUri("google");
        String params = String.join("&",
                "client_id=" + googleClientId,
                "redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8),
                "response_type=code",
                "scope=openid%20email%20profile",
                "access_type=online",
                "prompt=select_account");
        response.sendRedirect("https://accounts.google.com/o/oauth2/v2/auth?" + params);
    }

    @GetMapping("/google/callback")
    public void googleCallback(@RequestParam("code") String code,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        // 1. Intercambiar code por tokens
        String redirectUri = getRedirectUri("google");
        String body = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
                + "&client_id=" + URLEncoder.encode(googleClientId, StandardCharsets.UTF_8)
                + "&client_secret=" + URLEncoder.encode(googleClientSecret, StandardCharsets.UTF_8)
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&grant_type=authorization_code";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest tokenReq = HttpRequest.newBuilder()
                .uri(URI.create("https://oauth2.googleapis.com/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> tokenResp = client.send(tokenReq, HttpResponse.BodyHandlers.ofString());

        JsonNode tokenData = objectMapper.readTree(tokenResp.body());
        String idToken = tokenData.path("id_token").asText("");
        if (idToken.isEmpty()) {
            response.sendRedirect(frontendUrl + "/login?error=auth_failed");
            return;
        }

        // 2. Verificar el ID token
        JsonNode payload = verifyGoogleToken(idToken);
        if (payload == null) {
            response.sendRedirect(frontendUrl + "/login?error=auth_failed");
            return;
        }

        // 3. Crear/vincular usuario y generar JWT
        String email = payload.path("email").asText("");
        String name = payload.path("name").asText("");
        String picture = payload.path("picture").asText("");
        String sub = payload.path("sub").asText("");

        SocialAuthService.SocialResult result = socialAuthService.findOrCreate(
                "google", sub, email, name, picture);
        AuthResult tokens = authService.loginWithUser(result.user().getId());

        // 4. Redirigir al frontend con el token en el hash
        response.sendRedirect(frontendUrl + "/login#access_token=" + tokens.accessToken()
                + "&refresh_token=" + tokens.refreshToken());
    }

    // ===================== FACEBOOK =====================

    @GetMapping("/facebook")
    public void facebookAuth(HttpServletResponse response) throws IOException {
        String redirectUri = getRedirectUri("facebook");
        String params = String.join("&",
                "client_id=" + facebookAppId,
                "redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8),
                "response_type=code",
                "scope=public_profile%2Cemail");
        response.sendRedirect("https://www.facebook.com/v18.0/dialog/oauth?" + params);
    }

    @GetMapping("/facebook/callback")
    public void facebookCallback(@RequestParam("code") String code,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String redirectUri = getRedirectUri("facebook");
        String body = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
                + "&client_id=" + URLEncoder.encode(facebookAppId, StandardCharsets.UTF_8)
                + "&client_secret=" + URLEncoder.encode(facebookAppSecret, StandardCharsets.UTF_8)
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest tokenReq = HttpRequest.newBuilder()
                .uri(URI.create("https://graph.facebook.com/v18.0/oauth/access_token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> tokenResp = client.send(tokenReq, HttpResponse.BodyHandlers.ofString());

        JsonNode tokenData = objectMapper.readTree(tokenResp.body());
        String accessToken = tokenData.path("access_token").asText("");
        if (accessToken.isEmpty()) {
            response.sendRedirect(frontendUrl + "/login?error=auth_failed");
            return;
        }

        // Obtener datos del usuario
        HttpRequest userReq = HttpRequest.newBuilder()
                .uri(URI.create("https://graph.facebook.com/me?fields=id,name,email,picture&access_token=" + accessToken))
                .GET()
                .build();
        HttpResponse<String> userResp = client.send(userReq, HttpResponse.BodyHandlers.ofString());
        JsonNode payload = objectMapper.readTree(userResp.body());

        String email = payload.path("email").asText("");
        String name = payload.path("name").asText("");
        String picture = payload.path("picture").path("data").path("url").asText("");
        String sub = payload.path("id").asText("");

        SocialAuthService.SocialResult result = socialAuthService.findOrCreate(
                "facebook", sub, email, name, picture);
        AuthResult tokens = authService.loginWithUser(result.user().getId());

        response.sendRedirect(frontendUrl + "/login#access_token=" + tokens.accessToken()
                + "&refresh_token=" + tokens.refreshToken());
    }

    // ===================== ONBOARDING =====================

    @PostMapping("/onboarding")
    public ResponseEntity<Void> onboarding(@Valid @RequestBody OnboardingRequest request) {
        socialAuthService.assignInstitution(request.userId(), request.institutionId());
        return ResponseEntity.noContent().build();
    }

    // ===================== HELPERS =====================

    private String getRedirectUri(String provider) {
        // El redirect URI debe ser el del backend (que esta en Google Cloud Console)
        return "http://localhost:8081/api/v1/auth/oauth2/" + provider + "/callback";
    }

    private JsonNode verifyGoogleToken(String credential) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://oauth2.googleapis.com/tokeninfo?id_token=" + credential))
                .GET()
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) return null;
        JsonNode node = objectMapper.readTree(resp.body());
        if (googleClientId != null && !googleClientId.isEmpty()) {
            String aud = node.path("aud").asText("");
            if (!aud.equals(googleClientId)) return null;
        }
        return node;
    }

    public record OnboardingRequest(@NotBlank UUID userId, @NotBlank UUID institutionId) {
    }
}
