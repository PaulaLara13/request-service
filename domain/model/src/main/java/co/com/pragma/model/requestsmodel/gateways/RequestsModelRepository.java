package co.com.pragma.model.requestsmodel.gateways;

import co.com.pragma.model.requestsmodel.RequestsModel;
import co.com.pragma.model.requestsmodel.paging.PageModel;
import reactor.core.publisher.Mono;

public interface RequestsModelRepository {

     Mono<RequestsModel> saveRequest(RequestsModel requestsModel);

     Mono<PageModel<RequestsModel>> findByEstados(
             java.util.List<RequestStatus> estados,
             int page,
             int size,
             java.util.List<TypeLoan> tiposPrestamo
     );
}
