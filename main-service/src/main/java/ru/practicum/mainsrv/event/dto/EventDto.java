package ru.practicum.mainsrv.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainsrv.event.enums.EventState;
import ru.practicum.mainsrv.event.location.Location;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    Long id;
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    @PositiveOrZero
    private Long category;

    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();

    private int confirmedRequests;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @Future
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    @Builder.Default
    private Boolean paid = false;

    @Builder.Default
    private Integer participantLimit = 0;

    @Builder.Default
    private Boolean requestModeration = true;

    @Builder.Default
    private EventState state = EventState.PENDING;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    private long views;
}