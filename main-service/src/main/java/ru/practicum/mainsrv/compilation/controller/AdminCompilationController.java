package ru.practicum.mainsrv.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsrv.compilation.CompilationService;
import ru.practicum.mainsrv.compilation.dto.NewCompilationDto;
import ru.practicum.mainsrv.compilation.dto.UpdateCompilationRequest;
import ru.practicum.mainsrv.compilation.dto.ViewCompilationDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ViewCompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Запрос на создание подборки событий: {}", newCompilationDto);
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationById(@PathVariable Long compId) {
        compilationService.deleteCompilationById(compId);
        log.info("Подборка событий с ID={} удалена", compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public ViewCompilationDto updateCompilation(@RequestBody @Valid UpdateCompilationRequest updCompilationRequest,
                                                @PathVariable Long compId) {
        log.info("Запрос на изменение подборки событий с ID={} новые данные:{}", compId, updCompilationRequest);
        return compilationService.updateCompilation(updCompilationRequest, compId);
    }
}