package ru.practicum.shareit.item;

import static ru.practicum.shareit.item.ItemServiceImpl.requests;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder().name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null).build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder().name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .request(itemDto.getRequestId() != null ? requests.get(itemDto.getRequestId()) : null).build();
    }
}
