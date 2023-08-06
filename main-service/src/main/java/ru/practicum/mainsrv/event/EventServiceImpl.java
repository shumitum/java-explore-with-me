package ru.practicum.mainsrv.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.mainsrv.category.CategoryService;
import ru.practicum.mainsrv.event.dto.EventDto;
import ru.practicum.mainsrv.event.dto.FullEventDto;
import ru.practicum.mainsrv.event.dto.UpdateEventDto;
import ru.practicum.mainsrv.event.dto.ViewEventDto;
import ru.practicum.mainsrv.event.enums.EventState;
import ru.practicum.mainsrv.event.enums.EventStateAction;
import ru.practicum.mainsrv.event.enums.SortBy;
import ru.practicum.mainsrv.exception.ConflictException;
import ru.practicum.mainsrv.request.Request;
import ru.practicum.mainsrv.request.RequestMapper;
import ru.practicum.mainsrv.request.RequestRepository;
import ru.practicum.mainsrv.request.RequestStatus;
import ru.practicum.mainsrv.request.dto.RequestDto;
import ru.practicum.mainsrv.request.dto.RequestStatusUpdateDto;
import ru.practicum.mainsrv.request.dto.RequestStatusUpdateResult;
import ru.practicum.mainsrv.user.User;
import ru.practicum.mainsrv.user.UserService;
import ru.practicum.mainsrv.utility.PageParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@ComponentScan(basePackages = {"ru.practicum.client"})
public class EventServiceImpl implements EventService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final RequestMapper requestMapper;
    private final UserService userService;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;

    @Override
    @Transactional
    public FullEventDto createEvent(EventDto eventDto, Long userId) {
        final User user = userService.findUserById(userId);
        final Event event = eventMapper.toEvent(eventDto);
        event.setInitiator(user);
        event.setCategory(categoryService.findCategoryById(eventDto.getCategory()));
        FullEventDto createdEvenDto = eventMapper.toFullEventDto(eventRepository.save(event));
        log.info("Создано новое событие: {}", createdEvenDto);
        return createdEvenDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewEventDto> getEventsByInitiatorId(Long userId, int from, int size) {
        return eventRepository.getEventsByInitiatorId(userId, PageParam.of(from, size)).stream()
                .map(eventMapper::toViewEventDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FullEventDto getEventById(Long userId, Long eventId) {
        userService.checkUserExistence(userId);
        checkThatUserIsEventCreator(userId, eventId);
        return eventMapper.toFullEventDto(findEventById(eventId));
    }

    @Override
    @Transactional
    public FullEventDto updateEvent(UpdateEventDto eventDto, Long userId, Long eventId) {
        Event updatingEvent = findEventById(eventId);
        checkThatUserIsEventCreator(userId, eventId);
        if (updatingEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event must not be published");
        }
        if (updatingEvent.getState().equals(EventState.CANCELED) || updatingEvent.getState().equals(EventState.PENDING)) {
            updateEventFields(eventDto, updatingEvent);
            if (eventDto.getStateAction() != null) {
                if (eventDto.getStateAction().equals(EventStateAction.CANCEL_REVIEW)) {
                    updatingEvent.setState(EventState.CANCELED);
                }
                if (eventDto.getStateAction().equals(EventStateAction.SEND_TO_REVIEW)) {
                    updatingEvent.setState(EventState.PENDING);
                }
            }
        } else {
            throw new IllegalArgumentException("Only pending or canceled events can be changed");
        }
        return eventMapper.toFullEventDto(updatingEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getEventRequests(Long userId, Long eventId) {
        checkThatUserIsEventCreator(userId, eventId);
        return requestRepository.getRequestsByEventId(eventId).stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullEventDto> searchEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        return eventRepository.searchEventsByAdmin(users, states, categories, rangeStart, rangeEnd, PageParam.of(from, size))
                .stream()
                .map(eventMapper::toFullEventDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FullEventDto updateEventByAdmin(UpdateEventDto eventDto, Long eventId) {
        Event updatingEvent = findEventById(eventId);
        if (eventDto.getStateAction() != null) {
            if (updatingEvent.getState().equals(EventState.PENDING)) {
                if (eventDto.getStateAction().equals(EventStateAction.PUBLISH_EVENT)) {
                    updatingEvent.setPublishedOn(LocalDateTime.now());
                    updatingEvent.setState(EventState.PUBLISHED);
                } else if (eventDto.getStateAction().equals(EventStateAction.REJECT_EVENT)) {
                    updatingEvent.setState(EventState.CANCELED);
                }
            } else {
                throw new ConflictException("Событие можно отклонить/опубликовать, только если оно не опубликовано");
            }
        }
        updateEventFields(eventDto, updatingEvent);
        return eventMapper.toFullEventDto(updatingEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullEventDto> searchEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Boolean onlyAvailable, SortBy sort, int from, int size,
                                           HttpServletRequest request) {
        PageRequest page = sort != null ? PageParam.of(from, size, sort.descending()) : PageParam.of(from, size);
        List<Event> events;
        if (onlyAvailable.equals(true)) {
            events = eventRepository.searchOnlyAvailableEvents(EventState.PUBLISHED, text, categories, paid,
                    Objects.requireNonNullElseGet(rangeStart, LocalDateTime::now), rangeEnd, page);
        } else {
            events = eventRepository.searchAnyEvents(EventState.PUBLISHED, text, categories, paid,
                    Objects.requireNonNullElseGet(rangeStart, LocalDateTime::now), rangeEnd, page);
        }
        saveStatistic(request);
        return events.stream()
                .map(eventMapper::toFullEventDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FullEventDto getEventByIdByAnyone(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.getEventByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NoSuchElementException(String.format("События с ID=%d не существует", eventId)));
        saveStatistic(request);
        setEventViews(event);
        return eventMapper.toFullEventDto(event);
    }

    @Override
    @Transactional
    public RequestStatusUpdateResult updateRequestsStatus(RequestStatusUpdateDto requestsUpdDto, Long userId, Long eventId) {
        checkThatUserIsEventCreator(userId, eventId);
        final Event event = findEventById(eventId);
        if (event.getParticipantLimit() == 0 && event.getRequestModeration().equals(false)) {
            return new RequestStatusUpdateResult();
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("достигнут лимит по заявкам на данное событие");
        }
        final List<Request> updatingRequests = requestRepository.getRequestsByIdIn(requestsUpdDto.getRequestIds());
        if (updatingRequests.stream().anyMatch(request -> !request.getStatus().equals(RequestStatus.PENDING))) {
            throw new ConflictException("статус можно изменить только у заявок, находящихся в состоянии ожидания");
        }
        long updateLimit = event.getParticipantLimit() - event.getConfirmedRequests();
        if (requestsUpdDto.getStatus().equals(RequestStatus.CONFIRMED)) {
            if (updatingRequests.size() <= updateLimit) {
                updatingRequests.forEach(request -> request.setStatus(RequestStatus.CONFIRMED));
            } else if (updatingRequests.size() > updateLimit) {
                updatingRequests.stream().limit(updateLimit).forEach(request -> request.setStatus(RequestStatus.CONFIRMED));
                updatingRequests.stream().skip(updateLimit).forEach(request -> request.setStatus(RequestStatus.REJECTED));
            }
        } else {
            updatingRequests.forEach(request -> request.setStatus(RequestStatus.REJECTED));
        }
        countAndSetEventConfirmedRequests(event);
        final List<RequestDto> confirmedRequests = requestRepository.getRequestsByEvent_IdAndStatus(eventId, RequestStatus.CONFIRMED)
                .stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
        final List<RequestDto> rejectedRequests = requestRepository.getRequestsByEvent_IdAndStatus(eventId, RequestStatus.REJECTED)
                .stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
        return new RequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    @Override
    @Transactional
    public void countAndSetEventConfirmedRequests(Event event) {
        event.setConfirmedRequests(requestRepository.countRequestsByEvent_IdAndStatus(event.getId(), RequestStatus.CONFIRMED));
    }

    @Transactional(readOnly = true)
    public Event findEventById(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException(String.format("События с ID=%d не существует", eventId)));
    }

    @Transactional
    public void updateEventFields(UpdateEventDto eventDto, Event updatingEvent) {
        Optional.ofNullable(eventDto.getAnnotation()).ifPresent(updatingEvent::setAnnotation);
        Optional.ofNullable(eventDto.getCategory()).ifPresent(categoryService::findCategoryById);
        Optional.ofNullable(eventDto.getDescription()).ifPresent(updatingEvent::setDescription);
        Optional.ofNullable(eventDto.getEventDate()).ifPresent(updatingEvent::setEventDate);
        Optional.ofNullable(eventDto.getLocation()).ifPresent(updatingEvent::setLocation);
        Optional.ofNullable(eventDto.getPaid()).ifPresent(updatingEvent::setPaid);
        Optional.ofNullable(eventDto.getParticipantLimit()).ifPresent(updatingEvent::setParticipantLimit);
        Optional.ofNullable(eventDto.getRequestModeration()).ifPresent(updatingEvent::setRequestModeration);
        Optional.ofNullable(eventDto.getTitle()).ifPresent(updatingEvent::setTitle);
    }

    @Transactional(readOnly = true)
    public void checkThatUserIsEventCreator(Long userId, Long eventId) {
        userService.checkUserExistence(userId);
        if (!findEventById(eventId).getInitiator().getId().equals(userId)) {
            throw new IllegalArgumentException("event doesn't belong to user");
        }
    }

    @Transactional
    public void saveStatistic(HttpServletRequest request) {
        statsClient.createEndpointHit(EndpointHitDto.builder()
                .app("main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @Transactional
    public void setEventViews(Event event) {
        List<ViewStatsDto> views = statsClient.getHitStatistics(event.getCreatedOn(), LocalDateTime.now(),
                List.of(String.format("/events/%d", event.getId())), true);
        if (!views.isEmpty() && (views.get(0).getHits() > 0)) {
            event.setViews(views.get(0).getHits());
        }
    }
}