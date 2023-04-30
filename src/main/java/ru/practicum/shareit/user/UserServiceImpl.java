package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final HashMap<Long, User> usersMap = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<User> getUsers() {
        return List.copyOf(usersMap.values());
    }

    @Override
    public User getUserById(Long userId) {
        if (!usersMap.containsKey(userId)) {
            throw new NotFoundException("User not found!");
        }
        return usersMap.get(userId);
    }

    @Override
    public User addUser(User user) throws DuplicateException {
        for (User us : usersMap.values()) {
            if (us.email.equals(user.getEmail())) {
                throw new DuplicateException("Email duplicates");
            }
        }
        user.setId(id++);
        usersMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) throws DuplicateException {
        User updUser = usersMap.get(userId);
        for (User us : usersMap.values()) {
            if (us.getId() != userId.longValue() && us.email.equals(user.getEmail())) {
                throw new DuplicateException("Email duplicates");
            }
        }
        if (user.getName() != null) {
            updUser.setName(user.name);
        }
        if (user.getEmail() != null) {
            updUser.setEmail(user.getEmail());
        }
        usersMap.put(userId, updUser);

        return getUserById(userId);
    }

    @Override
    public Boolean deleteUser(Long userId) {
        if (!usersMap.containsKey(userId)) {
            return false;
        }
        usersMap.remove(userId);
        return true;
    }
}
