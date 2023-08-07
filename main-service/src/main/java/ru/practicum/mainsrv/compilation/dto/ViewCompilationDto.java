package ru.practicum.mainsrv.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainsrv.event.dto.ViewEventDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewCompilationDto {
    private Long id;
    private Boolean pinned;
    private String title;
    private List<ViewEventDto> events;
}