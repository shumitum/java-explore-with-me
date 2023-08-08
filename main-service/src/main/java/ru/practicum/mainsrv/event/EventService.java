package ru.practicum.mainsrv.event;

import ru.practicum.mainsrv.event.dto.EventDto;
import ru.practicum.mainsrv.event.dto.FullEventDto;
import ru.practicum.mainsrv.event.dto.UpdateEventDto;
import ru.practicum.mainsrv.event.dto.ViewEventDto;
import ru.practicum.mainsrv.event.enums.EventState;
import ru.practicum.mainsrv.event.enums.SortBy;
import ru.practicum.mainsrv.request.dto.RequestStatusUpdateDto;
import ru.practicum.mainsrv.request.dto.RequestStatusUpdateResult;
import ru.practicum.mainsrv.request.dto.RequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    FullEventDto createEvent(EventDto eventDto, Long userId);

    List<ViewEventDto> getEventsByInitiatorId(Long userId, int from, int size);

    FullEventDto getEventById(Long userId, Long eventId);

    Event findEventById(long eventId);

    FullEventDto updateEvent(UpdateEventDto eventDto, Long userId, Long eventId);

    List<RequestDto> getEventRequests(Long userId, Long eventId);

    List<FullEventDto> searchEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    FullEventDto updateEventByAdmin(UpdateEventDto eventDto, Long eventId);

    List<FullEventDto> searchEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, Boolean onlyAvailable, SortBy sort, int from, int size,
                                    HttpServletRequest request);

    FullEventDto getEventByIdByAnyone(Long eventId, HttpServletRequest request);

    RequestStatusUpdateResult updateRequestsStatus(RequestStatusUpdateDto requestsUpdDto, Long userId, Long eventId);

    void countAndSetEventConfirmedRequests(Event event);
}