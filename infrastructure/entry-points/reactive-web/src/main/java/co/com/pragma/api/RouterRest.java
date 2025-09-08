package co.com.pragma.api;

import co.com.pragma.api.dto.CreateRequestDto;
import co.com.pragma.api.mapper.RequestMapper;
import co.com.pragma.model.requestsmodel.ApiResponse;
import co.com.pragma.usecase.requests.RequestsUseCase;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/request", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class RouterRest {
    private final RequestsUseCase requestsUseCase;
    private final RequestMapper requestMapper;
    private static final Logger log = LoggerFactory.getLogger(RouterRest.class);

    @PostMapping
    public Mono<ResponseEntity<ApiResponse>> createRequest(@RequestBody CreateRequestDto createRequestDto) {
        log.info("Iniciando creación de una nueva solicitud {}", createRequestDto);
        return requestsUseCase.registerRequest(requestMapper.toModel(createRequestDto))
                .map(apiResponse -> ResponseEntity.status(HttpStatus.CREATED).body(apiResponse));
    }

}
