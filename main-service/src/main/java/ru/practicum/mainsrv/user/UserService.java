package ru.practicum.mainsrv.user;

import ru.practicum.mainsrv.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> userIds, int from, int size);

    void deleteUserById(Long userId);

    User findUserById(long userId);

    void checkUserExistence(long userId);
}