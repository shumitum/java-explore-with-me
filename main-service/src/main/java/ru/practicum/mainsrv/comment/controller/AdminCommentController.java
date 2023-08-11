package ru.practicum.mainsrv.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsrv.comment.CommentService;
import ru.practicum.mainsrv.comment.dto.CommentVisibilityUpdateDto;
import ru.practicum.mainsrv.comment.dto.ViewCommentByAdminDto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentController {
    private final CommentService commentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ViewCommentByAdminDto> getCommentsByAdmin(@RequestParam(required = false) Boolean visible,
                                                          @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                          @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Администраторский запрос списка комментариев. Только видимые комментарии?:{}", visible);
        return commentService.getCommentsByAdmin(visible, from, size);
    }

    @PatchMapping("/hide")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewCommentByAdminDto> hideComments(@RequestBody @Valid CommentVisibilityUpdateDto hideCommentDto) {
        log.info("Администраторский запрос на сокрытие комментариев IDs:{}", hideCommentDto);
        return commentService.hideComments(hideCommentDto);
    }

    @PatchMapping("/display")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewCommentByAdminDto> displayComments(@RequestParam @NotEmpty List<Long> ids) {
        log.info("Администраторский запрос на отображение комментариев, {}", ids);
        return commentService.displayComments(ids);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentsByAdmin(@RequestParam @NotEmpty List<Long> ids) {
        log.info("Администраторский запрос на удаление комментариев, ID удаляемых комментариев: {}", ids);
        commentService.deleteCommentsByAdmin(ids);
    }
}