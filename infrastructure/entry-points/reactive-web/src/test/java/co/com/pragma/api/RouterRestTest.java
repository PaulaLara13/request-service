package co.com.pragma.api;

import co.com.pragma.api.dto.CreateRequestDto;
import co.com.pragma.api.exception.RequestExceptionHandler;
import co.com.pragma.api.mapper.RequestMapper;
import co.com.pragma.model.requestsmodel.ApiResponse;
import co.com.pragma.model.requestsmodel.RequestsModel;
import co.com.pragma.model.requestsmodel.gateways.TypeLoan;
import co.com.pragma.usecase.requests.RequestsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = RouterRest.class)
@Import({RequestExceptionHandler.class})
class RouterRestTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RequestsUseCase requestsUseCase;

    @MockBean
    private RequestMapper requestMapper;

    @Test
    @WithMockUser(roles = {"ASESOR"})
    void createRequest_success() {
        var dto = new CreateRequestDto(BigInteger.ONE, "1001", 5000.0, 12, TypeLoan.PERSONAL);
        var model = RequestsModel.builder().build();
        var response = new ApiResponse("Solicitud creada con éxito. ID: 1");

        when(requestMapper.toModel(any(CreateRequestDto.class))).thenReturn(model);
        when(requestsUseCase.registerRequest(model)).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/v1/request")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ApiResponse.class);
    }
}
