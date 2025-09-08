package co.com.pragma.api.dto;

import co.com.pragma.model.requestsmodel.gateways.RequestStatusRepository;
import co.com.pragma.model.requestsmodel.gateways.TypeLoanRepository;

import java.math.BigInteger;

public record RequestDto (
        BigInteger id,
        String documentoIdentidad,
        Double monto,
        Integer plazo,
        TypeLoanRepository TipoPrestamo,
        RequestStatusRepository estado
) {
}
