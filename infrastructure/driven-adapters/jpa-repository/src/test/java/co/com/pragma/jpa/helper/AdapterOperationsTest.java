package co.com.pragma.jpa.helper;

import co.com.pragma.jpa.JPARepository;
import co.com.pragma.jpa.JPARepositoryAdapter;
import co.com.pragma.jpa.entity.RequestEntity;
import co.com.pragma.model.requestsmodel.RequestsModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigInteger;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AdapterOperationsTest {

    @Mock
    private JPARepository repository;
    @Mock
    private ObjectMapper mapper;
    private JPARepositoryAdapter adapter;


    @BeforeEach
    void setUp() {
        repository = mock(JPARepository.class);
        mapper = mock(ObjectMapper.class);
        adapter = new JPARepositoryAdapter(repository, mapper);
    }

    @Test
    void saveRequest_success() {
        RequestsModel domainModel = new RequestsModel();
        RequestEntity entity = new RequestEntity();
        entity.setId(BigInteger.ONE);

        // Simular mapeos
        when(mapper.map(any(RequestsModel.class), eq(RequestEntity.class))).thenReturn(entity);
        when(mapper.map(any(RequestEntity.class), eq(RequestsModel.class))).thenReturn(domainModel);

        // Simular guardado en JPA
        when(repository.save(any(RequestEntity.class))).thenReturn(entity);

        Mono<RequestsModel> result = adapter.saveRequest(domainModel);

        StepVerifier.create(result)
                .expectNext(domainModel)
                .verifyComplete();

        verify(repository).save(entity);
    }
}
