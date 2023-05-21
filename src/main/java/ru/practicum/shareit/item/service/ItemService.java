package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItems(Long id);

    ItemDto getItemById(Long userId, Long itemId);

    List<ItemDto> searchItems(String text);

    ItemDto updateItem(Long id, ItemDto itemDto, Long itemId);

    ItemDto addItem(Long id, ItemDto itemDto);

    CommentDto addComment(Long id, Long itemId, CommentDto commentDto);

}