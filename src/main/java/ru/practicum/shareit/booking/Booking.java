package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@RequiredArgsConstructor
public class Booking {

    Long id;

    LocalDateTime start;

    LocalDateTime end;

    Item item;

    User booker;


}
