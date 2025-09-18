package co.com.pragma.api.dto;

import co.com.pragma.model.requestsmodel.gateways.RequestStatus;
import co.com.pragma.model.requestsmodel.gateways.TypeLoan;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public record RequestDto (
        @JsonProperty("id") BigInteger id,
        @JsonProperty("documento_identidad") String documentoIdentidad,
        @JsonProperty("monto") Double monto,
        @JsonProperty("plazo") Integer plazo,
        @JsonProperty("tipo_prestamo") TypeLoan tipoPrestamo,
        @JsonProperty("estado_solicitud") RequestStatus estado,
        @JsonProperty("email") String email,
        @JsonProperty("nombre") String nombre,
        @JsonProperty("tasa_interes") Double tasaInteres,
        @JsonProperty("salario_base") Double salarioBase,
        @JsonProperty("deuda_total_mensual_solicitudes_aprobadas") Double deudaTotalMensualSolicitudesAprobadas
) {
}
