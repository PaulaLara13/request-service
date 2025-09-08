package co.com.pragma.api;

import co.com.pragma.api.dto.CreateRequestDto;
import co.com.pragma.api.exception.RequestExceptionHandler;
import co.com.pragma.api.mapper.RequestMapper;
import co.com.pragma.model.requestsmodel.ApiResponse;
import co.com.pragma.model.requestsmodel.RequestsModel;
import co.com.pragma.usecase.requests.RequestsUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

@ContextConfiguration(classes = {RouterRest.class, RequestExceptionHandler.class})
@WebFluxTest
class RouterRestTest {
    @Autowired
    private WebTestClient webTestClient;

    private RequestsUseCase requestsUseCase;

    private RequestMapper requestMapper;

    @Test
    void createRequest_success() {
        CreateRequestDto dto = new CreateRequestDto();
        RequestsModel model = new RequestsModel();
        ApiResponse response = new ApiResponse("Solicitud creada con éxito");

        Mockito.when(requestMapper.toModel(any(CreateRequestDto.class))).thenReturn(model);
        Mockito.when(requestsUseCase.registerRequest(model)).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/v1/request")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ApiResponse.class)
                .value(apiResponse ->
                        org.assertj.core.api.Assertions.assertThat(apiResponse.getMensaje()).contains("Solicitud creada")
                );
    }


}
