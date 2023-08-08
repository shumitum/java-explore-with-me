package ru.practicum.mainsrv.compilation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainsrv.compilation.dto.NewCompilationDto;
import ru.practicum.mainsrv.compilation.dto.ViewCompilationDto;
import ru.practicum.mainsrv.event.EventMapper;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface CompilationMapper {

    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    ViewCompilationDto toViewCompilationDto(Compilation compilation);
}