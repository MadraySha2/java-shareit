package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItems(Long id, Pageable pageable);

    ItemDto getItemById(Long userId, Long itemId);

    List<ItemDto> searchItems(String text, Pageable pageable);

    ItemDto updateItem(Long id, ItemDto itemDto, Long itemId);

    ItemDto addItem(Long id, ItemDto itemDto);

    CommentDto addComment(Long id, Long itemId, CommentDto commentDto);

}