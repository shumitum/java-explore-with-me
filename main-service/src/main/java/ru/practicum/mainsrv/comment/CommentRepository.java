package ru.practicum.mainsrv.comment;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getCommentsByAuthorIdAndVisibleOrderByIdAsc(Long authorId, Boolean visible, PageRequest page);

    @Query("select c from Comment as c " +
            "where (c.author.id = :userId) " +
            "and (:text is NULL or upper(c.text) like upper(concat('%', :text, '%'))) " +
            "and (cast(:rangeStart as java.time.LocalDateTime) is NULL or c.created > :rangeStart) " +
            "and (cast(:rangeEnd as java.time.LocalDateTime) is NULL or c.created < :rangeEnd) " +
            "and (c.visible = true) " +
            "order by c.id")
    List<Comment> findInCommentsByParam(@Param("userId") Long userId,
                                        @Param("text") String text,
                                        @Param("rangeStart") LocalDateTime rangeStart,
                                        @Param("rangeEnd") LocalDateTime rangeEnd,
                                        PageRequest page);

    List<Comment> getCommentsByAuthorIdAndEventIdAndVisibleOrderByIdAsc(Long authorId, Long eventId,
                                                                          Boolean visible, PageRequest page);

    List<Comment> getCommentsByEventIdAndVisibleOrderByIdAsc(Long eventId, Boolean visible, PageRequest page);
}