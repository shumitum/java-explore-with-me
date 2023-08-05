package ru.practicum.mainsrv.event.cotroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsrv.event.EventService;
import ru.practicum.mainsrv.event.dto.*;
import ru.practicum.mainsrv.event.validation.TimeValidationService;
import ru.practicum.mainsrv.request.RequestService;
import ru.practicum.mainsrv.request.dto.RequestStatusUpdateResult;
import ru.practicum.mainsrv.request.dto.RequestDto;
import ru.practicum.mainsrv.request.dto.RequestStatusUpdateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.mainsrv.event.validation.TimeValidationService.MIN_HOURS_BEFORE_EVENT_FOR_USER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final TimeValidationService timeValidationService;
    private final RequestService requestService;
    private final EventService eventService;


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public FullEventDto createEvent(@RequestBody @Valid EventDto eventDto,
                                    @PathVariable Long userId) {
        log.info("Запрос на создание события: {}", eventDto);
        timeValidationService.validatePeriodBeforeEvent(eventDto.getEventDate(), MIN_HOURS_BEFORE_EVENT_FOR_USER);
        return eventService.createEvent(eventDto, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ViewEventDto> getEventsByUserId(@PathVariable Long userId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрошен список событий, добавленных пользователем c ID={}, from={}, size={}", userId, from, size);
        return eventService.getEventsByInitiatorId(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullEventDto getEventById(@PathVariable Long userId,
                                     @PathVariable Long eventId) {
        log.info("Запрошено событие с ID={}, пользователем с ID={}", eventId, userId);
        return eventService.getEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullEventDto updateEvent(@RequestBody @Valid UpdateEventDto updateEventDto,
                                    @PathVariable Long userId,
                                    @PathVariable Long eventId) {
        log.info("Запрос на изменение события с ID={}: {}", eventId, updateEventDto);
        timeValidationService.validatePeriodBeforeEvent(updateEventDto.getEventDate(), MIN_HOURS_BEFORE_EVENT_FOR_USER);
        return eventService.updateEvent(updateEventDto, userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getEventRequests(@PathVariable Long userId,
                                             @PathVariable Long eventId) {
        log.info("Запрошен список запросов на участие в событии ID={} пользователя ID={}", eventId, userId);
        return eventService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public RequestStatusUpdateResult updateRequestsStatus(@RequestBody @Valid RequestStatusUpdateDto requestsUpdDto,
                                                          @PathVariable Long userId,
                                                          @PathVariable Long eventId) {
        log.info("Запроc на обновление статуса заявок на участие в событии с ID={}, юзером с ID={}. requestsDto: {}",
                eventId, userId, requestsUpdDto);
        return eventService.updateRequestsStatus(requestsUpdDto, userId, eventId);
    }
}