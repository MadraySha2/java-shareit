package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class User {
    Long id;

    @NotNull
    String name;

    @NotNull
    @Email
    String email;
}
