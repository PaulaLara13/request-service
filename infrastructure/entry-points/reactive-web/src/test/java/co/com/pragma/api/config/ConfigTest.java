package co.com.pragma.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import org.springframework.security.test.context.support.WithMockUser;

@WebFluxTest(controllers = ConfigTest.TestController.class)
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @RestController
    static class TestController {
        @GetMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
        Mono<String> ok() {
            return Mono.just("ok");
        }
    }

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @WithMockUser(roles = {"ASESOR"})
    void givenRequest_whenApplySecurityHeaders_thenHeadersPresent() {
        webTestClient.get()
                .uri("/test")
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }
}