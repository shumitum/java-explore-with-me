package ru.practicum.mainsrv.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainsrv.comment.dto.*;
import ru.practicum.mainsrv.event.EventService;
import ru.practicum.mainsrv.exception.ConflictException;
import ru.practicum.mainsrv.exception.InvalidArgumentException;
import ru.practicum.mainsrv.user.UserService;
import ru.practicum.mainsrv.utility.PageParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "comments")
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
    @CachePut(key = "#commentId")
    public ViewCommentDto updateComment(CommentUpdateDto commentDto, Long userId, Long commentId) {
        checkThatUserIsCommentCreator(userId, commentId);
        final Comment updatingComment = findCommentById(commentId);
        if (updatingComment.getCreated().plusMinutes(15).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Комментарий можно редактировать только в течении первых 15 минут");
        }
        if (commentDto.getText() != null && !commentDto.getText().isEmpty() && commentDto.getText().length() <= 1000) {
            if (updatingComment.getVisible().equals(false)) {
                throw new ConflictException("Нельзя редактировать скрытые комментарии");
            }
            if (!updatingComment.getText().equals(commentDto.getText())) {
                updatingComment.setText(commentDto.getText());
            }
        } else {
            throw new InvalidArgumentException("Новый текст комментария не должен быть пуст или превышать 1000 символов");
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
    @CacheEvict(key = "#commentId")
    public void deleteComment(Long userId, Long commentId) {
        checkThatUserIsCommentCreator(userId, commentId);
        final Comment comment = findCommentById(commentId);
        if (comment.getCreated().plusMinutes(15).isAfter(LocalDateTime.now())) {
            commentRepository.deleteById(commentId);
        } else {
            throw new IllegalArgumentException("Комментарий можно удалить только в течении первых 15 минут");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewCommentDto> getEventComments(Long eventId, int from, int size) {
        eventService.checkEventExistence(eventId);
        return commentMapper.toViewCommentDtoList(commentRepository.getCommentsByEventIdAndVisibleOrderByIdAsc(eventId,
                true, PageParam.of(from, size)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewCommentByAdminDto> getCommentsByAdmin(Boolean visible, int from, int size) {
        return commentMapper.toViewCommentByAdminDtoList(commentRepository.getComments(visible, PageParam.of(from, size)));
    }

    @Override
    @Transactional
    public List<ViewCommentByAdminDto> hideComments(CommentVisibilityUpdateDto commentDto) {
        return commentMapper.toViewCommentByAdminDtoList(commentDto.getCommentIds().stream()
                .map(this::findCommentById)
                .peek(comment -> comment.setVisible(false))
                .peek(comment -> comment.setReason(commentDto.getReason()))
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public List<ViewCommentByAdminDto> displayComments(List<Long> ids) {
        return commentMapper.toViewCommentByAdminDtoList(ids.stream()
                .map(this::findCommentById)
                .peek(comment -> comment.setVisible(true))
                .peek(comment -> comment.setReason(null))
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void deleteCommentsByAdmin(List<Long> ids) {
        List<Long> deletedCommentIds = new ArrayList<>();
        List<Long> doesntExistCommentIds = new ArrayList<>();
        for (Long id : ids) {
            if (commentRepository.existsById(id)) {
                commentRepository.deleteById(id);
                deletedCommentIds.add(id);
            } else {
                doesntExistCommentIds.add(id);
            }
        }
        if (deletedCommentIds.isEmpty()) {
            throw new NoSuchElementException(String.format("Комментарии с указанными ID не существуют: %s", doesntExistCommentIds));
        }
        log.info("Удалены комментарии с ID: {}, ID несуществующих комментов: {}", deletedCommentIds, doesntExistCommentIds);
    }

    @Transactional(readOnly = true)
    @Cacheable
    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Комментария с ID=%d не существует", commentId)));
    }

    @Transactional(readOnly = true)
    public void checkThatUserIsCommentCreator(Long userId, Long commentId) {
        userService.checkUserExistence(userId);
        if (!findCommentById(commentId).getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("Данный комментарий написал другой пользователь");
        }
    }
}