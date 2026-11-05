package com.eduaircontrol.apigateway.config;

import com.eduaircontrol.apigateway.security.JwksProvider;
import com.eduaircontrol.apigateway.security.JwtValidator;
import com.eduaircontrol.apigateway.security.RemoteJwksProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewaySecurityConfig {

    @Bean
    JwksProvider jwksProvider(WebClient webClient, AppProperties properties) {
        return new RemoteJwksProvider(webClient, properties.jwt().jwksUri());
    }

    @Bean
    JwtValidator jwtValidator(JwksProvider jwksProvider) {
        return new JwtValidator(jwksProvider);
    }
}
