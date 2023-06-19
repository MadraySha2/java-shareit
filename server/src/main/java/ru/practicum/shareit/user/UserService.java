package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.DuplicateException;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    UserDto getUserById(Long userId);

    UserDto addUser(UserDto user) throws DuplicateException;

    UserDto updateUser(Long userId, UserDto user) throws DuplicateException;

    Boolean deleteUser(Long userId);
}
