package co.com.pragma.model.requestsmodel.gateways;

import co.com.pragma.model.requestsmodel.RequestsModel;
import reactor.core.publisher.Mono;

public interface RequestsModelRepository {

     Mono<RequestsModel> saveRequest(RequestsModel requestsModel);
}


