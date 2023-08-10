package ru.practicum.mainsrv.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainsrv.comment.ReasonForHiding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentVisibilityUpdateDto {
    @NotEmpty//(groups = {Hide.class, Display.class})
    private List<Long> commentIds;
    @NotNull//(groups = Hide.class)
    private ReasonForHiding reason;
}