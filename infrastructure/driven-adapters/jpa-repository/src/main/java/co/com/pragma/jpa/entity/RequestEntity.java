package co.com.pragma.jpa.entity;

import co.com.pragma.model.requestsmodel.gateways.RequestStatusRepository;
import co.com.pragma.model.requestsmodel.gateways.TypeLoanRepository;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Table(name = "requests")
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private BigInteger id;
    private String documentoIdentidad;
    private Double monto;
    private Integer plazo;
    private TypeLoanRepository TipoPrestamo;
    private RequestStatusRepository estado;

}
