package com.eduaircontrol.mssecurity.infrastructure.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OAuth2EndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void openIdConfigurationIsPublic() throws Exception {
        mockMvc.perform(get("/.well-known/openid-configuration"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.issuer").value("http://localhost:8081"))
                .andExpect(jsonPath("$.jwks_uri").value("http://localhost:8081/oauth2/jwks"))
                .andExpect(jsonPath("$.authorization_endpoint").exists())
                .andExpect(jsonPath("$.token_endpoint").exists());
    }

    @Test
    void oauth2JwksExposesRsaKey() throws Exception {
        mockMvc.perform(get("/oauth2/jwks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keys[0].kty").value("RSA"))
                .andExpect(jsonPath("$.keys[0].kid").isString());
    }

    @Test
    void loginPageRendersCompanyCodeForm() throws Exception {
        mockMvc.perform(get("/login").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("password")));
    }

    @Test
    void authorizeWithoutSessionRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/oauth2/authorize")
                        .accept(MediaType.TEXT_HTML)
                        .queryParam("response_type", "code")
                        .queryParam("client_id", "ea-control-web")
                        .queryParam("redirect_uri", "http://localhost:8080/login/oauth2/code/web")
                        .queryParam("scope", "openid")
                        .queryParam("code_challenge", "E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM")
                        .queryParam("code_challenge_method", "S256"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
