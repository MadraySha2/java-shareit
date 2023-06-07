package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ItemRequestsService {

    ItemRequestDto addRequest(Long id, ItemRequestDto itemRequestDto);

    List<ItemRequestWithItems> getOwnRequests(Long id, PageRequest pageRequest);

    List<ItemRequestWithItems> getAll(Long id, PageRequest pageRequest);

    ItemRequestWithItems getRequestById(Long id, Long requestId);
}
