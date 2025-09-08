package co.com.pragma.model.requestsmodel;

import co.com.pragma.model.requestsmodel.gateways.RequestStatusRepository;
import co.com.pragma.model.requestsmodel.gateways.TypeLoanRepository;
import lombok.*;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestsModel {
    private BigInteger id;
    private String documentoIdentidad;
    private Double monto;
    private Integer plazo;
    private TypeLoanRepository tipoPrestamo;
    private RequestStatusRepository estado;
}
