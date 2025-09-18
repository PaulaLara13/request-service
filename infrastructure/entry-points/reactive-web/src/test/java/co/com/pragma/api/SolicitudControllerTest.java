package co.com.pragma.api;

import co.com.pragma.api.dto.RequestDto;
import co.com.pragma.api.mapper.RequestMapper;
import co.com.pragma.model.requestsmodel.RequestsModel;
import co.com.pragma.model.requestsmodel.gateways.RequestStatus;
import co.com.pragma.model.requestsmodel.gateways.TypeLoan;
import co.com.pragma.model.requestsmodel.paging.PageModel;
import co.com.pragma.usecase.requests.RequestsUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest
@Import({co.com.pragma.api.config.SecurityConfig.class, co.com.pragma.api.exception.RequestExceptionHandler.class, SolicitudController.class})
class SolicitudControllerTest {

    @SpringBootConfiguration
    static class TestBootConfig {}

    private static final String BASE_URL = "/api/v1/solicitud";
    private static final String HEADER_CT = "Content-Type";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RequestsUseCase requestsUseCase;

    @MockBean
    private RequestMapper requestMapper;

    @Test
    @WithMockUser(roles = {"ASESOR"})
    void givenValidParams_whenListar_thenReturnOkWithPage() {
        var model = RequestsModel.builder()
                .id(BigInteger.ONE)
                .documentoIdentidad("1001")
                .monto(5000.0)
                .plazo(12)
                .tipoPrestamo(TypeLoan.PERSONAL)
                .estado(RequestStatus.PENDIENTE_REVISION)
                .email("ana@example.com")
                .nombre("Ana")
                .tasaInteres(1.5)
                .salarioBase(2500.0)
                .deudaTotalMensualSolicitudesAprobadas(300.0)
                .build();
        var pageModel = new PageModel<>(List.of(model), 0, 10, 1, 1);
        when(requestsUseCase.listarSolicitudesParaRevision(eq(0), eq(10), any(), any())).thenReturn(Mono.just(pageModel));
        var dto = new RequestDto(BigInteger.ONE, "1001", 5000.0, 12, TypeLoan.PERSONAL, RequestStatus.PENDIENTE_REVISION, "ana@example.com", "Ana", 1.5, 2500.0, 300.0);
        when(requestMapper.toResponseList(any())).thenReturn(List.of(dto));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URL)
                        .queryParam("page", 0)
                        .queryParam("size", 10)
                        .queryParam("estado", "PENDIENTE_REVISION,RECHAZADO")
                        .queryParam("tipoPrestamo", "PERSONAL")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(HEADER_CT, MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.content[0].documento_identidad").isEqualTo("1001")
                .jsonPath("$.content[0].tipo_prestamo").isEqualTo("PERSONAL")
                .jsonPath("$.content[0].estado_solicitud").isEqualTo("PENDIENTE_REVISION")
                .jsonPath("$.page").isEqualTo(0)
                .jsonPath("$.size").isEqualTo(10)
                .jsonPath("$.totalElements").isEqualTo(1)
                .jsonPath("$.totalPages").isEqualTo(1);
    }

    @Test
    @WithMockUser(roles = {"ASESOR"})
    void givenInvalidPage_whenListar_thenReturnBadRequest() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URL)
                        .queryParam("page", -1)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithMockUser(roles = {"ASESOR"})
    void givenInvalidSize_whenListar_thenReturnBadRequest() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URL)
                        .queryParam("page", 0)
                        .queryParam("size", 0)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
