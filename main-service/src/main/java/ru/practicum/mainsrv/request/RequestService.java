package ru.practicum.mainsrv.request;

import ru.practicum.mainsrv.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto createRequest(Long eventId, Long userId);

    RequestDto cancelRequest(Long requestId, Long userId);

    List<RequestDto> getRequestsByRequesterId(Long userId);
}