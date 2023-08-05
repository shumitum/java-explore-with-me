package ru.practicum.mainsrv.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainsrv.category.Category;
import ru.practicum.mainsrv.user.dto.UserInfoDto;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewEventDto {
    Long id;
    private String annotation;
    private Category category;
    int confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    UserInfoDto initiator;
    private Boolean paid;
    private String title;
    private long views;
}