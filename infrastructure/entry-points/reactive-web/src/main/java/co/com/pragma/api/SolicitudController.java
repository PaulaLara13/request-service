package co.com.pragma.api;

import co.com.pragma.api.dto.PageResponse;
import co.com.pragma.api.dto.RequestDto;
import co.com.pragma.api.mapper.RequestMapper;
import co.com.pragma.model.requestsmodel.RequestsModel;
import co.com.pragma.model.requestsmodel.gateways.TypeLoan;
import co.com.pragma.model.requestsmodel.gateways.RequestStatus;
import co.com.pragma.model.requestsmodel.paging.PageModel;
import co.com.pragma.usecase.requests.RequestsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/solicitud", produces = MediaType.APPLICATION_JSON_VALUE)
public class SolicitudController {

    private static final Logger log = LoggerFactory.getLogger(SolicitudController.class);
    private final RequestsUseCase requestsUseCase;
    private final RequestMapper requestMapper;

    public SolicitudController(RequestsUseCase requestsUseCase, RequestMapper requestMapper) {
        this.requestsUseCase = requestsUseCase;
        this.requestMapper = requestMapper;
    }

    @GetMapping
    public Mono<PageResponse<RequestDto>> listar(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "estado", required = false) String estadosCsv,
            @RequestParam(name = "tipoPrestamo", required = false) String tipoPrestamoCsv
    ) {
        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page must be >= 0");
        }
        if (size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "size must be > 0");
        }
        List<RequestStatus> estados = parseEstados(estadosCsv);
        List<TypeLoan> tipos = parseTipos(tipoPrestamoCsv);
        log.info("GET /api/v1/solicitud page={}, size={}, estados={}, tipos={} ", page, size, estados, tipos);
        return requestsUseCase.listarSolicitudesParaRevision(page, size, estados, tipos)
                .map(this::toPageResponse);
    }

    private PageResponse<RequestDto> toPageResponse(PageModel<RequestsModel> pageModel) {
        List<RequestDto> dtos = requestMapper.toResponseList(pageModel.getContent());
        return new PageResponse<>(dtos, pageModel.getPage(), pageModel.getSize(), pageModel.getTotalElements(), pageModel.getTotalPages());
    }

    private List<TypeLoan> parseTipos(String csv) {
        if (csv == null || csv.isBlank()) {
            return List.of();
        }
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(String::toUpperCase)
                .map(TypeLoan::valueOf)
                .toList();
    }

    private List<RequestStatus> parseEstados(String csv) {
        if (csv == null || csv.isBlank()) {
            return List.of();
        }
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(String::toUpperCase)
                .map(RequestStatus::valueOf)
                .toList();
    }
}
