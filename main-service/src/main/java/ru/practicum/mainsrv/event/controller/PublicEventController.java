package ru.practicum.mainsrv.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsrv.event.EventService;
import ru.practicum.mainsrv.event.dto.FullEventDto;
import ru.practicum.mainsrv.event.enums.SortBy;
import ru.practicum.mainsrv.event.validation.TimeValidationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {
    private final EventService eventService;
    private final TimeValidationService timeValidationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FullEventDto> searchEvents(@RequestParam(required = false) String text,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false) Boolean paid,
                                           @RequestParam(required = false) LocalDateTime rangeStart,
                                           @RequestParam(required = false) LocalDateTime rangeEnd,
                                           @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                           @RequestParam(required = false) SortBy sort,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size,
                                           HttpServletRequest request) {
        log.info("Поиск событий с параметрами text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}," +
                " sort={}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);
        timeValidationService.checkStartTimeIsBeforeEndTime(rangeStart, rangeEnd);
        return eventService.searchEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullEventDto getEventByIdByAnyone(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("Запрошено событие с ID={}", eventId);
        return eventService.getEventByIdByAnyone(eventId, request);
    }
}
