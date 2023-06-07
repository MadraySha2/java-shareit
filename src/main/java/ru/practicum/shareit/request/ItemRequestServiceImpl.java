package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemForRequest;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.request.ItemRequestMapper.*;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestsService {

    private final ItemRequestsRepository itemRequestsRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    public ItemRequestDto addRequest(Long id, ItemRequestDto itemRequestDto) {
        ItemRequest item = toRequest(itemRequestDto);
        item.setRequestor(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User Not found!")));
        item.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        return toRequestDto(itemRequestsRepository.save(item));
    }


    @Override
    public List<ItemRequestWithItems> getOwnRequests(Long id, PageRequest pageRequest) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User Not found!");
        }
        Page<ItemRequestWithItems> requests = itemRequestsRepository
                .findByRequestor_id(id, pageRequest).map(this::setItems);
        return requests.stream().collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestWithItems> getAll(Long id, PageRequest pageRequest) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User Not found!");
        }
        return itemRequestsRepository.findAllByRequestorIdNot(id, pageRequest)
                .map(this::setItems).stream().collect(Collectors.toList());
    }

    @Override
    public ItemRequestWithItems getRequestById(Long id, Long requestId) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User Not found!");
        }
        return itemRequestsRepository.findById(requestId)
                .map(this::setItems)
                .orElseThrow(() -> new NotFoundException("Request not found!"));
    }


    private ItemRequestWithItems setItems(ItemRequest itemRequest) {
        ItemRequestWithItems request = toRequestWithItems(itemRequest);
        List<ItemForRequest> items = itemRepository
                .findAllByRequestId(itemRequest.getId())
                .stream().map(ItemMapper::toItemForRequest).collect(Collectors.toList());

        if (items.isEmpty()) {
            request.setItems(new ArrayList<>());
        } else {
            request.setItems(items);
        }
        return request;
    }

}