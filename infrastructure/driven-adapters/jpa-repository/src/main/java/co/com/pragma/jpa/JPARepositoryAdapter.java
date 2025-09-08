package co.com.pragma.jpa;

import co.com.pragma.jpa.entity.RequestEntity;
import co.com.pragma.jpa.helper.AdapterOperations;
import co.com.pragma.model.requestsmodel.RequestsModel;
import co.com.pragma.model.requestsmodel.gateways.RequestsModelRepository;
import jakarta.transaction.Transactional;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigInteger;

@Repository
public class JPARepositoryAdapter extends AdapterOperations<RequestsModel, RequestEntity, BigInteger, JPARepository> implements RequestsModelRepository
// implements ModelRepository from domain
{

    public JPARepositoryAdapter(JPARepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, RequestsModel.class/* change for domain model */));
    }

    @Transactional
    @Override
    public Mono<RequestsModel> saveRequest(RequestsModel requestsModel) {
        return Mono.fromCallable(() -> {
            RequestEntity requestEntity = repository.save(toData(requestsModel));
            return toEntity(requestEntity);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
