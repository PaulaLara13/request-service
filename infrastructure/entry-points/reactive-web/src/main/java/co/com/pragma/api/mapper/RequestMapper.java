package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.CreateRequestDto;
import co.com.pragma.api.dto.RequestDto;
import co.com.pragma.model.requestsmodel.RequestsModel;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestDto toResponse(RequestsModel requestsModel);

    //List<RequestDto> toResponseList(List<RequestsModel> requestsModelList);

    RequestsModel toModel(CreateRequestDto createRequestDto);
}
