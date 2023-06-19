package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ItemRequestMapper {

    @Autowired
    private UserService userService;

    public static ItemRequestDto toRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor() != null ? itemRequest.getRequestor() : null)
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequest toRequest(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated() != null ? itemRequestDto.getCreated()
                        : Timestamp.valueOf(LocalDateTime.now()))
                .requestor(itemRequestDto.getRequestor() != null ? itemRequestDto.getRequestor() : null)
                .build();
    }

    public static ItemRequestWithItems toRequestWithItems(ItemRequest itemRequest) {
        return ItemRequestWithItems.builder()
                .id(itemRequest.getId())
                .created(itemRequest.getCreated())
                .description(itemRequest.getDescription())
                .build();
    }

}
