package ru.practicum.mainsrv.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsrv.comment.CommentService;
import ru.practicum.mainsrv.comment.dto.ViewCommentDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ViewCommentDto> getEventComments(@RequestParam Long eventId,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос комментариев, оставленных к событию с ID={}", eventId);
        return commentService.getEventComments(eventId, from, size);
    }
}