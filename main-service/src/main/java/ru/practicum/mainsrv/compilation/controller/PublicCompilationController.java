package ru.practicum.mainsrv.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsrv.compilation.CompilationService;
import ru.practicum.mainsrv.compilation.dto.ViewCompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public ViewCompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Запрошена подборка событий с ID={}", compId);
        return compilationService.getCompilationById(compId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ViewCompilationDto> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                    @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрошена подборка событий: закреплена?={}, from={}, size={}", pinned, from, size);
        return compilationService.getCompilations(pinned, from, size);
    }
}