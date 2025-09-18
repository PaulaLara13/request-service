package co.com.pragma.jpa;

import co.com.pragma.jpa.entity.RequestEntity;
import co.com.pragma.jpa.helper.AdapterOperations;
import co.com.pragma.model.requestsmodel.RequestsModel;
import co.com.pragma.model.requestsmodel.paging.PageModel;
import co.com.pragma.model.requestsmodel.gateways.RequestsModelRepository;
import co.com.pragma.model.requestsmodel.gateways.RequestStatus;
import co.com.pragma.model.requestsmodel.gateways.TypeLoan;
import jakarta.transaction.Transactional;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigInteger;
import java.util.List;

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

    @Override
    public Mono<PageModel<RequestsModel>> findByEstados(List<RequestStatus> estados, int page, int size, List<TypeLoan> tiposPrestamo) {
        return Mono.fromCallable(() -> {
            Pageable pageable = PageRequest.of(page, size);
            var pageResult = repository.findByEstadoIn(estados, pageable);
            var filtered = pageResult.getContent().stream()
                    .filter(e -> tiposPrestamo == null || tiposPrestamo.isEmpty() || (e.getTipoPrestamo() != null && tiposPrestamo.contains(e.getTipoPrestamo())))
                    .map(this::toEntity)
                    .toList();

            // Si hubo filtrado adicional, el total podría cambiar; para simplicidad usamos totalElements del repositorio
            return new PageModel<>(filtered, pageResult.getNumber(), pageResult.getSize(), pageResult.getTotalElements(), pageResult.getTotalPages());
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
