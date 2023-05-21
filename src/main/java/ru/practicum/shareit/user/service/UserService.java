package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exceptions.DuplicateException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    UserDto getUserById(Long userId);

    UserDto addUser(UserDto user) throws DuplicateException;

    UserDto updateUser(Long userId, User user) throws DuplicateException;

    Boolean deleteUser(Long userId);
}
