package ru.practicum.mainsrv.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainsrv.request.dto.RequestDto;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(target = "event", source = "request.event.id")
    @Mapping(target = "requester", source = "request.requester.id")
    RequestDto toRequestDto(Request request);
}