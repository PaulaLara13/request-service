package co.com.pragma.jpa;

import co.com.pragma.jpa.entity.RequestEntity;
import co.com.pragma.model.requestsmodel.gateways.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.math.BigInteger;
import java.util.List;

public interface JPARepository extends JpaRepository<RequestEntity, BigInteger>, QueryByExampleExecutor<RequestEntity> {
    Page<RequestEntity> findByEstadoIn(List<RequestStatus> estados, Pageable pageable);
}
