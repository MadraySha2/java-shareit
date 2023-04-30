package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.DuplicateException;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    User getUserById(Long userId);

    User addUser(User user) throws DuplicateException;

    User updateUser(Long userId, User user) throws DuplicateException;

    Boolean deleteUser(Long userId);
}
