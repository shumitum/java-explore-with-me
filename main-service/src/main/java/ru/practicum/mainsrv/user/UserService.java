package ru.practicum.mainsrv.user;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> userIds, int from, int size);

    void deleteUserById(Long userId);
}