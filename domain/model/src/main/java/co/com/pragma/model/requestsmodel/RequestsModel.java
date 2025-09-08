package co.com.pragma.model.requestsmodel;

import co.com.pragma.model.requestsmodel.gateways.RequestStatus;
import co.com.pragma.model.requestsmodel.gateways.TypeLoan;
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
    private TypeLoan tipoPrestamo;
    private RequestStatus estado;
}
