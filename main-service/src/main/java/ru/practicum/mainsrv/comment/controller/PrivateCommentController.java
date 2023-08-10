package ru.practicum.mainsrv.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsrv.comment.CommentService;
import ru.practicum.mainsrv.comment.dto.CommentDto;
import ru.practicum.mainsrv.comment.dto.CommentUpdateDto;
import ru.practicum.mainsrv.comment.dto.ViewCommentDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ViewCommentDto createComment(@RequestBody @Valid CommentDto commentDto,
                                        @PathVariable Long userId) {
        log.info("Запрос на создание комментария к событию с ID={}, от пользователя с ID={}, {}",
                commentDto.getEventId(), userId, commentDto);
        return commentService.createComment(commentDto, userId);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ViewCommentDto updateComment(@RequestBody @Valid CommentUpdateDto commentDto,
                                        @PathVariable Long userId,
                                        @PathVariable Long commentId) {
        log.info("Запрос на редактирование комментария с ID={} от пользователя с ID={}, {}", commentId, userId, commentDto);
        return commentService.updateComment(commentDto, userId, commentId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewCommentDto> getAllUserComments(@PathVariable Long userId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                   @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос всех комментариев, оставленных пользователем с ID={}, from={}, size={}", userId, from, size);
        return commentService.getAllUserComments(userId, from, size);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewCommentDto> findInUserComments(@PathVariable Long userId,
                                                   @RequestParam(required = false) String text,
                                                   @RequestParam(required = false) LocalDateTime rangeStart,
                                                   @RequestParam(required = false) LocalDateTime rangeEnd,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                   @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("ПОИСК по комментариям, оставленных пользователем с ID={}, text={}, rangeStart={}," +
                " rangeEnd={}, from={}, size={}", userId, text, rangeStart, rangeEnd, from, size);
        return commentService.findInUserComments(userId, text, rangeStart, rangeEnd, from, size);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ViewCommentDto> getUserCommentsForEvent(@PathVariable Long userId,
                                                        @RequestParam Long eventId,
                                                        @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                        @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос всех комментариев, оставленных пользователем с ID={} к событию с ID={}, from={}, size={}",
                userId, eventId, from, size);
        return commentService.getUserCommentsForEvent(userId, eventId, from, size);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
        log.info("Комментарий с ID={} удалён", commentId);
    }
}