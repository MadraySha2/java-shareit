package shareitgateway.item;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {


    private final ItemClient client;

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") Long id) {
        return client.getItems(id);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return client.getItemById(userId, itemId);
    }

    @Validated
    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") Long id, @RequestParam @DefaultValue(value = " ") String text, @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from, @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return client.searchItems(id, text, from, size);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") @DefaultValue(value = "0L") Long id, @RequestBody ItemDto itemDto, @PathVariable Long itemId) {
        return client.updateItem(id, itemDto, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Long id, @Valid @RequestBody ItemDto itemDto) {
        return client.addItem(id, itemDto);
    }

    @Validated
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long id, @PathVariable Long itemId, @Valid @RequestBody CommentDto commentDto) {
        return client.addComment(id, itemId, commentDto);
    }

}
