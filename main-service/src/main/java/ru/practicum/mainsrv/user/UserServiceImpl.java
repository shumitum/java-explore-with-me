package ru.practicum.mainsrv.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainsrv.user.dto.UserDto;
import ru.practicum.mainsrv.utility.PageParam;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        UserDto newUserDto = userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
        log.info("Создан новый пользователь: {}", newUserDto);
        return newUserDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> userIds, int from, int size) {
        return userRepository.getUsersByIdsIn(userIds, PageParam.of(from, size)).stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        checkUserExistence(userId);
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Пользователя с ID=%d не существует", userId)));
    }

    @Override
    @Transactional(readOnly = true)
    public void checkUserExistence(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException(String.format("Пользователя с ID=%d не существует", userId));
        }
    }
}