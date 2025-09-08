package co.com.pragma.api.dto;

import co.com.pragma.model.requestsmodel.gateways.RequestStatus;
import co.com.pragma.model.requestsmodel.gateways.TypeLoan;

import java.math.BigInteger;

public record RequestDto (
        BigInteger id,
        String documentoIdentidad,
        Double monto,
        Integer plazo,
        TypeLoan TipoPrestamo,
        RequestStatus estado
) {
}
