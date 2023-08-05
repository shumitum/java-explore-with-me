package ru.practicum.mainsrv.event.cotroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsrv.event.EventService;
import ru.practicum.mainsrv.event.dto.FullEventDto;
import ru.practicum.mainsrv.event.dto.UpdateEventDto;
import ru.practicum.mainsrv.event.enums.EventState;
import ru.practicum.mainsrv.event.validation.TimeValidationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.mainsrv.event.validation.TimeValidationService.MIN_HOURS_BEFORE_EVENT_FOR_ADMIN;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService eventService;
    private final TimeValidationService timeValidationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FullEventDto> searchEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                                  @RequestParam(required = false) List<EventState> states,
                                                  @RequestParam(required = false) List<Long> categories,
                                                  @RequestParam(required = false) LocalDateTime rangeStart,
                                                  @RequestParam(required = false) LocalDateTime rangeEnd,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Поиск событий администратором");//насыпать параметров в логи
        timeValidationService.checkStartTimeIsBeforeEndTime(rangeStart, rangeEnd);
        return eventService.searchEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullEventDto updateEventByAdmin(@RequestBody @Valid UpdateEventDto updateEventDto,
                                           @PathVariable Long eventId) {
        log.info("Запрос на изменение события с ID={} от администратора: {}", eventId, updateEventDto);
        timeValidationService.validatePeriodBeforeEvent(updateEventDto.getEventDate(), MIN_HOURS_BEFORE_EVENT_FOR_ADMIN);
        return eventService.updateEventByAdmin(updateEventDto, eventId);
    }
}