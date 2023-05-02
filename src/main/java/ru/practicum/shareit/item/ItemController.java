package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {


    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long id) {
        return itemService.getItems(id);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam @DefaultValue(value = " ") String text) {
        return itemService.searchItems(text);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") @DefaultValue(value = "0L") Long id,
                              @RequestBody ItemDto itemDto, @PathVariable Long itemId) {
        return itemService.updateItem(id, itemDto, itemId);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long id, @Valid @RequestBody ItemDto itemDto) {
        return itemService.addItem(id, itemDto);
    }

}
