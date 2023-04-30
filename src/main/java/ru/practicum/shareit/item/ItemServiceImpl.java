package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final HashMap<Long, ItemDto> itemsMap = new HashMap<>();

    @Autowired
    private final UserService userService;

    private Long id = 1L;

    public List<ItemDto> getItems(Long id) {
        List<ItemDto> items = new ArrayList<>();
        for (ItemDto itemDto : List.copyOf(itemsMap.values())) {
            if (itemDto.getOwner().getId() == id) {
                items.add(itemDto);
            }
        }
        return items;
    }

    public ItemDto getItemById(Long itemId) {
        if (!itemsMap.containsKey(itemId)) {
            throw new NotFoundException("Item not found!");
        }
        return itemsMap.get(itemId);
    }

    public List<ItemDto> searchItems(String text) {
        List<ItemDto> items = new ArrayList<>();
        if (!text.isBlank()) {
            for (ItemDto itemDto : itemsMap.values()) {
                if ((itemDto.getName().toLowerCase().contains(text.toLowerCase())
                        || itemDto.getDescription().toLowerCase().contains(text.toLowerCase())) && itemDto.getAvailable()) {
                    items.add(itemDto);
                }
            }
        }
        return items;
    }

    public ItemDto updateItem(Long id, ItemDto itemDto, Long itemId) {
        if (!itemsMap.containsKey(itemId)) {
            throw new NotFoundException("Item not found!"); // не оч понимаю, как реализовать это по другому, поэтому
            //                                                    тут так много if-ов, но возможно смогу еще что-то придумать
        }
        ItemDto item = itemsMap.get(itemId);
        if (item.getOwner().getId() != id) {
            throw new NotFoundException("Another owner!");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        itemsMap.put(itemId, item);
        return item;
    }

    public ItemDto addItem(Long userId, ItemDto itemDto) {
        userService.getUserById(userId);
        itemDto.setId(id++);
        itemDto.setOwner(userService.getUserById(userId));
        itemsMap.put(itemDto.getId(), itemDto);
        return getItemById(itemDto.getId());
    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder().name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null).build();
    }

}
