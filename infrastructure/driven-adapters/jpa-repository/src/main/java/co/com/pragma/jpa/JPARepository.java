package co.com.pragma.jpa;

import co.com.pragma.jpa.entity.RequestEntity;
import co.com.pragma.model.requestsmodel.RequestsModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.math.BigInteger;

public interface JPARepository extends CrudRepository<RequestEntity, BigInteger>, QueryByExampleExecutor<RequestEntity> {
}
