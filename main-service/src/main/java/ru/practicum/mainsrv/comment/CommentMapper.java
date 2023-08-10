package ru.practicum.mainsrv.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainsrv.comment.dto.CommentDto;
import ru.practicum.mainsrv.comment.dto.ViewCommentByAdminDto;
import ru.practicum.mainsrv.comment.dto.ViewCommentDto;
import ru.practicum.mainsrv.user.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {
    Comment toComment(CommentDto commentDto);

    @Mapping(target = "author", source = "comment.author")
    @Mapping(target = "eventId", source = "comment.event.id")
    ViewCommentDto toViewCommentDto(Comment comment);

    @Mapping(target = "author", source = "comment.author")
    @Mapping(target = "eventId", source = "comment.event.id")
    ViewCommentByAdminDto toViewCommentByAdminDto(Comment comment);

    @Mapping(target = "author", source = "comment.author")
    @Mapping(target = "eventId", source = "comment.event.id")
    List<ViewCommentDto> toViewCommentDtoList(List<Comment> comments);
}