package ru.practicum.mainsrv.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainsrv.event.dto.EventDto;
import ru.practicum.mainsrv.event.dto.FullEventDto;
import ru.practicum.mainsrv.event.dto.ViewEventDto;

@Mapper(componentModel = "spring")
public interface EventMapper {
    ViewEventDto toViewEventDto(Event event);

    FullEventDto toFullEventDto(Event event);

    @Mapping(target = "category.id", source = "eventDto.category")
    Event toEvent(EventDto eventDto);
}