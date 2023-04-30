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
    public User getUserById(Long user_id) {
        if (!usersMap.containsKey(user_id)) {
            throw new NotFoundException("User not found!");
        }
        return usersMap.get(user_id);
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
    public User updateUser(Long user_id, User user) throws DuplicateException {
        User updUser = usersMap.get(user_id);
        for (User us : usersMap.values()) {
            if (us.getId() != user_id && us.email.equals(user.getEmail())) {
                throw new DuplicateException("Email duplicates");
            }
        }
        if (user.getName() != null) {
            updUser.setName(user.name);
        }
        if (user.getEmail() != null) {
            updUser.setEmail(user.getEmail());
        }
        usersMap.put(user_id, updUser);

        return getUserById(user_id);
    }

    @Override
    public Boolean deleteUser(Long user_id) {
        if (!usersMap.containsKey(user_id)) {
            return false;
        }
        usersMap.remove(user_id);
        return true;
    }
}
