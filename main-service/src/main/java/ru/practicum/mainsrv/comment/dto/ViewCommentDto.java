package ru.practicum.mainsrv.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainsrv.user.dto.UserInfoDto;

import java.time.LocalDateTime;

import static ru.practicum.dto.EndpointHitDto.DATE_TIME_PATTERN;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewCommentDto {
    private Long id;
    private String text;
    private UserInfoDto author;
    private Long eventId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime created;
}