package co.com.pragma.model.requestsmodel;

public class ApiResponse {
    private final String mensaje;

    public ApiResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}