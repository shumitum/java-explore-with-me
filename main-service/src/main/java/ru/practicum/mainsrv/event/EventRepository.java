package ru.practicum.mainsrv.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainsrv.event.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> getEventsByInitiatorId(Long userId, PageRequest page);

    Optional<Event> getEventByIdAndState(Long eventId, EventState state);

    List<Event> getEventsByIdIn(List<Long> evenIds);

    Boolean existsEventByCategory_Id(Long catId);

    @Query("select e from Event as e " +
            "where ((:users) is NULL or e.initiator.id in :users) " +
            "and ((:states) is NULL or e.state in :states) " +
            "and ((:categories) is NULL or e.category.id in :categories) " +
            "and (cast(:rangeStart as java.time.LocalDateTime) is NULL or e.eventDate > :rangeStart) " +
            "and (cast(:rangeEnd as java.time.LocalDateTime) is NULL or e.eventDate < :rangeEnd)")
    List<Event> searchEventsByAdmin(@Param("users") List<Long> users,
                                    @Param("states") List<EventState> states,
                                    @Param("categories") List<Long> categories,
                                    @Param("rangeStart") LocalDateTime rangeStart,
                                    @Param("rangeEnd") LocalDateTime rangeEnd,
                                    PageRequest page);

    @Query("select e from Event as e " +
            "where (e.state = :state) " +
            "and (:text is NULL or upper(e.annotation) like upper(concat('%', :text, '%'))) " +
            "or (:text is NULL or upper(e.annotation) like upper(concat('%', :text, '%'))) " +
            "and ((:categories) is NULL or e.category.id in :categories) " +
            "and ((:paid) is NULL or e.paid = :paid) " +
            "and (cast(:rangeStart as java.time.LocalDateTime) is NULL or e.eventDate > :rangeStart) " +
            "and (cast(:rangeEnd as java.time.LocalDateTime) is NULL or e.eventDate < :rangeEnd) " +
            "and ((:paid) is NULL or e.paid = :paid) " +
            "and (e.participantLimit > e.confirmedRequests)")
    List<Event> searchOnlyAvailableEvents(@Param("state") EventState state,
                                          @Param("text") String text,
                                          @Param("categories") List<Long> categories,
                                          @Param("paid") Boolean paid,
                                          @Param("rangeStart") LocalDateTime rangeStart,
                                          @Param("rangeEnd") LocalDateTime rangeEnd,
                                          PageRequest page);

    @Query("select e from Event as e " +
            "where (e.state = :state) " +
            "and (:text is NULL or upper(e.annotation) like upper(concat('%', :text, '%'))) " +
            "or (:text is NULL or upper(e.annotation) like upper(concat('%', :text, '%'))) " +
            "and ((:categories) is NULL or e.category.id in :categories) " +
            "and ((:paid) is NULL or e.paid = :paid) " +
            "and (cast(:rangeStart as java.time.LocalDateTime) is NULL or e.eventDate > :rangeStart) " +
            "and (cast(:rangeEnd as java.time.LocalDateTime) is NULL or e.eventDate < :rangeEnd) " +
            "and ((:paid) is NULL or e.paid = :paid)")
    List<Event> searchAnyEvents(@Param("state") EventState state,
                                @Param("text") String text,
                                @Param("categories") List<Long> categories,
                                @Param("paid") Boolean paid,
                                @Param("rangeStart") LocalDateTime rangeStart,
                                @Param("rangeEnd") LocalDateTime rangeEnd,
                                PageRequest page);
}