package ru.practicum.mainsrv.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Boolean existsRequestByEventIdAndRequesterId(Long eventId, Long userId);

    List<Request> getRequestsByRequesterId(Long userId);

    List<Request> getRequestsByEventId(Long eventId);

    List<Request> getRequestsByIdInAndStatus(List<Long> requestIds, RequestStatus status);

    List<Request> getRequestsByIdIn(List<Long> requestIds);

    Long countRequestsByEvent_IdAndStatus(Long eventId, RequestStatus status);

    List<Request> getRequestsByEvent_IdAndStatus(Long eventId, RequestStatus status);
}