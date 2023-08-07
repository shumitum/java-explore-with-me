package ru.practicum.mainsrv.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainsrv.category.Category;
import ru.practicum.mainsrv.event.enums.EventState;
import ru.practicum.mainsrv.event.location.Location;
import ru.practicum.mainsrv.user.dto.UserInfoDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullEventDto {
    private Long id;
    private String annotation;
    private Category category;
    private int confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserInfoDto initiator;
    private Location location;
    private Boolean paid;
    private int participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private long views;
}