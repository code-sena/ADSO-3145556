package com.eduaircontrol.apigateway.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

class PublicPathsTest {

    @Test
    void healthAndAuthEntryPointsArePublic() {
        assertThat(PublicPaths.isPublic(HttpMethod.GET, "/health")).isTrue();
        assertThat(PublicPaths.isPublic(HttpMethod.GET, "/api/v1/auth/health")).isTrue();
        assertThat(PublicPaths.isPublic(HttpMethod.POST, "/api/v1/auth/register")).isTrue();
        assertThat(PublicPaths.isPublic(HttpMethod.POST, "/api/v1/auth/login")).isTrue();
        assertThat(PublicPaths.isPublic(HttpMethod.POST, "/api/v1/auth/refresh")).isTrue();
        assertThat(PublicPaths.isPublic(HttpMethod.GET, "/api/v1/auth/jwks")).isTrue();
    }

    @Test
    void preflightRequestsArePublic() {
        assertThat(PublicPaths.isPublic(HttpMethod.OPTIONS, "/api/v1/sensors")).isTrue();
    }

    @Test
    void oauth2LoginPathsArePublic() {
        assertThat(PublicPaths.isPublic(HttpMethod.GET, "/oauth2/authorization/web")).isTrue();
        assertThat(PublicPaths.isPublic(HttpMethod.GET, "/login/oauth2/code/web")).isTrue();
    }

    @Test
    void logoutAndBusinessRoutesRequireAuthentication() {
        assertThat(PublicPaths.isPublic(HttpMethod.POST, "/api/v1/auth/logout")).isFalse();
        assertThat(PublicPaths.isPublic(HttpMethod.GET, "/api/v1/sensors")).isFalse();
        assertThat(PublicPaths.isPublic(HttpMethod.GET, "/api/v1/users/1")).isFalse();
    }
}
