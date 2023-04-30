package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.DuplicateException;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<User> getUsers() {
        return service.getUsers();
    }

    @GetMapping("/{user_id}")
    public User getUserById(@PathVariable Long user_id) {
        return service.getUserById(user_id);
    }

    @DeleteMapping("/{user_id}")
    public Boolean deleteUser(@PathVariable Long user_id) {
        return service.deleteUser(user_id);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws DuplicateException {
        return service.addUser(user);
    }

    @PatchMapping("/{user_id}")
    public User updateUser(@PathVariable Long user_id, @RequestBody User user) throws DuplicateException {
        return service.updateUser(user_id, user);
    }
}
