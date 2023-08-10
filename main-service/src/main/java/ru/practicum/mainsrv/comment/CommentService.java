package ru.practicum.mainsrv.comment;

import ru.practicum.mainsrv.comment.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    ViewCommentDto createComment(CommentDto commentDto, Long userId);

    ViewCommentDto updateComment(CommentUpdateDto commentDto, Long userId, Long commentId);

    List<ViewCommentDto> getAllUserComments(Long userId, int from, int size);

    List<ViewCommentDto> findInUserComments(Long userId, String text, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, int from, int size);

    List<ViewCommentDto> getUserCommentsForEvent(Long userId, Long eventId, int from, int size);

    void deleteComment(Long userId, Long commentId);

    List<ViewCommentDto> getEventComments(Long eventId, int from, int size);

    List<ViewCommentByAdminDto> getCommentsByAdmin(Boolean visible, int from, int size);

    List<ViewCommentByAdminDto> hideComments(CommentVisibilityUpdateDto commentDto);

    List<ViewCommentByAdminDto> displayComments(List<Long> ids);

    void deleteCommentsByAdmin(List<Long> ids);
}