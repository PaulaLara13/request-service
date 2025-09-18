package co.com.pragma.usecase.requests;

import co.com.pragma.model.requestsmodel.ApiResponse;
import co.com.pragma.model.requestsmodel.RequestsModel;
import co.com.pragma.model.requestsmodel.gateways.RequestStatus;
import co.com.pragma.model.requestsmodel.gateways.RequestsModelRepository;
import co.com.pragma.model.requestsmodel.gateways.TypeLoan;
import co.com.pragma.model.requestsmodel.paging.PageModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static co.com.pragma.common.Constants.*;

@RequiredArgsConstructor
public class RequestsUseCase {

    private final RequestsModelRepository requestsModelRepository;

    public Mono<ApiResponse> registerRequest(RequestsModel requestsModel) {
        validateFields(requestsModel);
        requestsModel.setEstado(RequestStatus.PENDIENTE_REVISION);

        return requestsModelRepository.saveRequest(requestsModel)
                .map(saved -> {
                    String mensaje = (saved.getId() != null)
                            ? REQUESTS_CREATEID + saved.getId()
                            : REQUESTS_CREATE_NOTID;
                    return new ApiResponse(mensaje);
                });
    }

    public Mono<PageModel<RequestsModel>> listarSolicitudesParaRevision(
            int page,
            int size,
            List<RequestStatus> estados,
            List<TypeLoan> tiposPrestamo
    ) {
        var estadosDefecto = List.of(RequestStatus.PENDIENTE_REVISION, RequestStatus.RECHAZADO, RequestStatus.REVISION_MANUAL);
        var estadosAplicados = (estados == null || estados.isEmpty()) ? estadosDefecto : estados;
        return requestsModelRepository.findByEstados(estadosAplicados, page, size, tiposPrestamo);
    }

    private void validateFields(RequestsModel requestsModel){
        if(requestsModel.getMonto() == null || requestsModel.getMonto() <= 0){
            throw new IllegalArgumentException(AMOUNT_ZERO);
        }
        if(requestsModel.getPlazo() == null || requestsModel.getPlazo() <= 0){
            throw new IllegalArgumentException(TIME_LIMIT_ZERO);
        }
        if (requestsModel.getTipoPrestamo() == null  ||
                Arrays.stream(TypeLoan.values()).noneMatch((t -> t.equals(requestsModel.getTipoPrestamo())))) {
            throw new IllegalArgumentException(INVALID_LOAN);
        }
    }


}
