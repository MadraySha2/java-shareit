package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    Long id;

    String name;

    String description;

    Boolean available;

    User owner;

    ItemRequest request;
}
