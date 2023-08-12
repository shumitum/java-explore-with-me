package ru.practicum.statsrv.stats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.statsrv.stats.model.EndpointHit;

import static ru.practicum.dto.EndpointHitDto.DATE_TIME_PATTERN;

@Mapper(componentModel = "spring")
public interface EndpointHitMapper {
    @Mapping(target = "timestamp", source = "timestamp", dateFormat = DATE_TIME_PATTERN)
    EndpointHit toEndpointHit(EndpointHitDto endpointHitDto);
}