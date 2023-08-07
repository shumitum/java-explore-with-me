package ru.practicum.mainsrv.compilation;

import ru.practicum.mainsrv.compilation.dto.NewCompilationDto;
import ru.practicum.mainsrv.compilation.dto.UpdateCompilationRequest;
import ru.practicum.mainsrv.compilation.dto.ViewCompilationDto;

import java.util.List;

public interface CompilationService {
    ViewCompilationDto createCompilation(NewCompilationDto newCompilationDto);

    ViewCompilationDto getCompilationById(Long compId);

    List<ViewCompilationDto> getCompilations(Boolean pinned, int from, int size);

    void deleteCompilationById(Long compId);

    ViewCompilationDto updateCompilation(UpdateCompilationRequest updCompilationRequest, Long compId);
}