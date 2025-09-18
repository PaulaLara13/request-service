package co.com.pragma.usecase.requests;

import co.com.pragma.model.requestsmodel.ApiResponse;
import co.com.pragma.model.requestsmodel.RequestsModel;
import co.com.pragma.model.requestsmodel.gateways.RequestStatus;
import co.com.pragma.model.requestsmodel.gateways.RequestsModelRepository;
import co.com.pragma.model.requestsmodel.gateways.TypeLoan;
import co.com.pragma.model.requestsmodel.paging.PageModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;
import java.util.List;

import static co.com.pragma.common.Constants.AMOUNT_ZERO;
import static co.com.pragma.common.Constants.INVALID_LOAN;
import static co.com.pragma.common.Constants.TIME_LIMIT_ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestsUseCaseTest {

    private static final String MSG_PREFIX = "Solicitud creada con éxito. ID: ";

    @Mock
    private RequestsModelRepository repository;

    @InjectMocks
    private RequestsUseCase useCase;

    @Test
    void givenValidRequest_whenRegisterRequest_thenReturnApiResponseWithId() {
        var model = RequestsModel.builder()
                .id(null)
                .documentoIdentidad("1001")
                .monto(1000.0)
                .plazo(12)
                .tipoPrestamo(TypeLoan.PERSONAL)
                .build();
        var saved = model.toBuilder().id(BigInteger.ONE).estado(RequestStatus.PENDIENTE_REVISION).build();
        when(repository.saveRequest(any())).thenReturn(Mono.just(saved));

        var result = useCase.registerRequest(model);

        StepVerifier.create(result)
                .assertNext(r -> {
                    assertEquals(MSG_PREFIX + "1", ((ApiResponse) r).getMensaje());
                })
                .verifyComplete();

        ArgumentCaptor<RequestsModel> captor = ArgumentCaptor.forClass(RequestsModel.class);
        verify(repository, times(1)).saveRequest(captor.capture());
        assertEquals(RequestStatus.PENDIENTE_REVISION, captor.getValue().getEstado());
    }

    @Test
    void givenZeroAmount_whenRegisterRequest_thenThrow() {
        var model = RequestsModel.builder()
                .monto(0.0)
                .plazo(12)
                .tipoPrestamo(TypeLoan.PERSONAL)
                .build();

        var ex = assertThrows(IllegalArgumentException.class, () -> useCase.registerRequest(model));
        assertEquals(AMOUNT_ZERO, ex.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void givenZeroTerm_whenRegisterRequest_thenThrow() {
        var model = RequestsModel.builder()
                .monto(1000.0)
                .plazo(0)
                .tipoPrestamo(TypeLoan.PERSONAL)
                .build();

        var ex = assertThrows(IllegalArgumentException.class, () -> useCase.registerRequest(model));
        assertEquals(TIME_LIMIT_ZERO, ex.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void givenInvalidLoan_whenRegisterRequest_thenThrow() {
        var model = RequestsModel.builder()
                .monto(1000.0)
                .plazo(6)
                .tipoPrestamo(null)
                .build();

        var ex = assertThrows(IllegalArgumentException.class, () -> useCase.registerRequest(model));
        assertEquals(INVALID_LOAN, ex.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void givenFilters_whenListarSolicitudesParaRevision_thenDelegatesToRepository() {
        var estados = List.of(RequestStatus.PENDIENTE_REVISION, RequestStatus.RECHAZADO);
        var tipos = List.of(TypeLoan.PERSONAL, TypeLoan.CONSUMO);
        var pageModel = new PageModel<RequestsModel>(List.of(), 0, 10, 0, 0);
        when(repository.findByEstados(estados, 0, 10, tipos)).thenReturn(Mono.just(pageModel));

        var result = useCase.listarSolicitudesParaRevision(0, 10, estados, tipos);

        StepVerifier.create(result)
                .expectNext(pageModel)
                .verifyComplete();

        verify(repository, times(1)).findByEstados(estados, 0, 10, tipos);
    }
}
