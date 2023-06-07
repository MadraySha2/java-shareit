package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemForRequest {

    private Long id;

    private Long ownerId;

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;

}
