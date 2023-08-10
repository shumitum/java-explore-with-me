package ru.practicum.mainsrv.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainsrv.comment.dto.*;
import ru.practicum.mainsrv.event.EventService;
import ru.practicum.mainsrv.exception.ConflictException;
import ru.practicum.mainsrv.user.UserService;
import ru.practicum.mainsrv.utility.PageParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventService eventService;
    private final UserService userService;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public ViewCommentDto createComment(CommentDto commentDto, Long userId) {
        final Comment comment = commentMapper.toComment(commentDto);
        comment.setAuthor(userService.findUserById(userId));
        comment.setEvent(eventService.findEventById(commentDto.getEventId()));
        final Comment newComment = commentRepository.save(comment);
        log.info("Создан новый комментарий: {}", newComment);
        return commentMapper.toViewCommentDto(newComment);
    }

    @Override
    @Transactional
    public ViewCommentDto updateComment(CommentUpdateDto commentDto, Long userId, Long commentId) {
        checkThatUserIsCommentCreator(userId, commentId);
        final Comment updatingComment = findCommentById(commentId);
        if (updatingComment.getCreated().plusMinutes(15).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Комментарий можно редактировать только в течении первых 15 минут");
        }
        if (commentDto.getText() != null) {
            if (updatingComment.getVisible().equals(false)) {
                throw new ConflictException("Нельзя редактировать скрытые комментарии");
            }
            if (!updatingComment.getText().equals(commentDto.getText())) {
                updatingComment.setText(commentDto.getText());
            }
        }
        log.info("Комментарий отредактирован: {}", updatingComment);
        return commentMapper.toViewCommentDto(updatingComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewCommentDto> getAllUserComments(Long userId, int from, int size) {
        userService.checkUserExistence(userId);
        return commentMapper.toViewCommentDtoList(commentRepository.getCommentsByAuthorIdAndVisibleOrderByIdAsc(userId,
                true, PageParam.of(from, size)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewCommentDto> findInUserComments(Long userId, String text, LocalDateTime rangeStart,
                                                   LocalDateTime rangeEnd, int from, int size) {
        userService.checkUserExistence(userId);
        return commentMapper.toViewCommentDtoList(commentRepository.findInCommentsByParam(userId, text, rangeStart,
                rangeEnd, PageParam.of(from, size)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewCommentDto> getUserCommentsForEvent(Long userId, Long eventId, int from, int size) {
        userService.checkUserExistence(userId);
        eventService.checkEventExistence(eventId);
        return commentMapper.toViewCommentDtoList(commentRepository.getCommentsByAuthorIdAndEventIdAndVisibleOrderByIdAsc(userId,
                eventId, true, PageParam.of(from, size)));
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        checkThatUserIsCommentCreator(userId, commentId);
        final Comment comment = findCommentById(commentId);
        if (comment.getCreated().plusMinutes(15).isAfter(LocalDateTime.now())) {
            commentRepository.deleteById(commentId);
        } else {
            throw new ConflictException("Комментарий можно удалить только в течении первых 15 минут");
        }
    }

    @Override
    public List<ViewCommentDto> getEventComments(Long eventId, int from, int size) {
        eventService.checkEventExistence(eventId);
        return commentMapper.toViewCommentDtoList(commentRepository.getCommentsByEventIdAndVisibleOrderByIdAsc(eventId,
                true, PageParam.of(from, size)));
    }

    @Override
    @Transactional
    public List<ViewCommentByAdminDto> getCommentsByAdmin(Boolean visible, int from, int size) {
        return null;
    }

    @Override
    @Transactional
    public List<ViewCommentByAdminDto> hideComments(CommentVisibilityUpdateDto commentDto) {
        return null;
    }

    @Override
    @Transactional
    public List<ViewCommentByAdminDto> displayComments(List<Long> ids) {
        return null;
    }

    @Override
    @Transactional
    public void deleteCommentsByAdmin(List<Long> ids) {

    }

    @Transactional(readOnly = true)
    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Комментария с ID=%d не существует", commentId)));
    }

    @Transactional(readOnly = true)
    public void checkCommentExistence(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NoSuchElementException(String.format("Комментария с ID=%d не существует", commentId));
        }
    }

    @Transactional(readOnly = true)
    public void checkThatUserIsCommentCreator(Long userId, Long commentId) {
        userService.checkUserExistence(userId);
        if (!findCommentById(commentId).getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("Данный комментарий написал другой пользователь");
        }
    }
}