package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.DuplicateException;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    User getUserById(Long user_id);

    User addUser(User user) throws DuplicateException;

    User updateUser(Long user_id, User user) throws DuplicateException;

    Boolean deleteUser(Long user_id);
}
