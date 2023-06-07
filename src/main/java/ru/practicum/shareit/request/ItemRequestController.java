package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestsService requestService;

    @PostMapping()
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long id,
                                     @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return requestService.addRequest(id, itemRequestDto);
    }

    @GetMapping()
    public List<ItemRequestWithItems> getOwnRequests(@RequestHeader("X-Sharer-User-Id") Long id,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) throws ValidationException {

        if (from < 0 || size < 0) {
            throw new ValidationException("");
        }
        return requestService.getOwnRequests(id, PageRequest
                .of(from, size).withSort(Sort.by("created").descending()));
    }

    @GetMapping("/all")
    public List<ItemRequestWithItems> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long id,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) throws ValidationException {
        if (from < 0 || size < 0) {
            throw new ValidationException("");
        }
        return requestService.getAll(id, PageRequest
                .of(from > 0 ? from / size : 0, size, Sort.by(Sort.Direction.DESC, "created")));
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithItems getRequestById(@RequestHeader("X-Sharer-User-Id")
                                               Long id, @PathVariable Long requestId) {
        return requestService.getRequestById(id, requestId);
    }

}
