package ru.practicum.mainsrv.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsrv.request.dto.RequestDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestController {
    private final RequestService requestService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable Long userId,
                                    @RequestParam Long eventId) {
        log.info("Запрос на участии в событии ID={} от пользователя с ID={}", eventId, userId);
        return requestService.createRequest(eventId, userId);
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public RequestDto cancelRequest(@PathVariable Long userId,
                                    @PathVariable Long requestId) {
        log.info("Запрос на отмену заявки с ID={} на участие в событии от пользователя с ID={}", requestId, userId);
        return requestService.cancelRequest(requestId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getRequestsByRequesterId(@PathVariable Long userId) {
        log.info("Запрошен список заявок пользователя c ID={} на участие в событиях", userId);
        return requestService.getRequestsByRequesterId(userId);
    }
}