package ru.practicum.mainsrv.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainsrv.event.Event;
import ru.practicum.mainsrv.event.EventService;
import ru.practicum.mainsrv.event.enums.EventState;
import ru.practicum.mainsrv.exception.ValidationException;
import ru.practicum.mainsrv.request.dto.RequestDto;
import ru.practicum.mainsrv.user.User;
import ru.practicum.mainsrv.user.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final EventService eventService;
    private final UserService userService;

    @Override
    @Transactional
    public RequestDto createRequest(Long eventId, Long userId) {
        final Event event = eventService.findEventById(eventId);
        final User user = userService.findUserById(userId);
        if (requestRepository.existsRequestByEventIdAndRequesterId(eventId, userId).equals(true)) {
            throw new ValidationException("нельзя добавить повторный запрос");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("нельзя участвовать в неопубликованном событии");
        }
        if (event.getParticipantLimit() != 0 && (event.getConfirmedRequests() >= event.getParticipantLimit())) {
            throw new ValidationException("достигнут лимит запросов на участие");
        }
        RequestStatus status;
        if (event.getRequestModeration().equals(false) || event.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
        } else {
            status = RequestStatus.PENDING;
        }
        Request request = Request.builder()
                .event(event)
                .requester(user)
                .status(status).build();
        Request newRequest = requestRepository.save(request);
        eventService.countAndSetEventConfirmedRequests(event);
        return requestMapper.toRequestDto(newRequest);
    }

    @Override
    @Transactional
    public RequestDto cancelRequest(Long requestId, Long userId) {
        Request request = findRequestById(requestId);
        if (request.getRequester().getId().equals(userId)) {
            request.setStatus(RequestStatus.CANCELED);
        } else {
            throw new IllegalArgumentException("that request doesn't belong to user");
        }
        Event event = eventService.findEventById(request.getEvent().getId());
        eventService.countAndSetEventConfirmedRequests(event);
        return requestMapper.toRequestDto(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequestsByRequesterId(Long userId) {
        return requestRepository.getRequestsByRequesterId(userId).stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Request findRequestById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Заявки с ID=%d не существует", requestId)));
    }
}