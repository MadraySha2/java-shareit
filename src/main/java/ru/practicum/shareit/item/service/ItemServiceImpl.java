package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exceptions.NotAvailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.CommentRepository;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.dto.CommentMapper.toCommentDto;
import static ru.practicum.shareit.item.dto.ItemMapper.toItem;
import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.dto.UserMapper.toUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final CommentRepository commentRepository;

    @Autowired
    private final UserService userService;


    public List<ItemDto> getItems(Long id) {
        return itemRepository.findAllByOwnerId(id)
                .stream()
                .map(ItemMapper::toItemDto).map(this::setBookings).map(this::setComments).collect(Collectors.toList());
    }

    public ItemDto getItemById(Long userId, Long itemId) {
        ItemDto item = toItemDto(itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found")));
        if (item.getOwner().getId().longValue() == userId.longValue()) {
            item = setBookings(item);
        }
        item = (setComments(item));
        return item;
    }

    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.findByNameOrDescriptionAvailable(text)
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public ItemDto updateItem(Long id, ItemDto itemDto, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("item not found"));
        if (item.getOwner().getId() != id.longValue()) {
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
        return toItemDto(itemRepository.save(item));
    }


    @Transactional
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        itemDto.setOwner(toUser(userService.getUserById(userId)));
        return toItemDto(itemRepository.save(toItem(itemDto)));
    }

    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        Comment comment = Comment.builder().text(commentDto.getText()).build();
        comment.setAuthor(toUser(userService.getUserById(userId)));
        comment.setItem((itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("No such item"))));
        if (!bookingRepository.existsByBookerIdAndEndBeforeAndStatus(userId, LocalDateTime.now(), Status.APPROVED)) {
            throw new NotAvailableException("You cant comment before use!");
        }

        comment.setCreated(LocalDateTime.now());
        return toCommentDto(commentRepository.save(comment));
    }

    public ItemDto setBookings(ItemDto itemDto) {
        itemDto.setLastBooking(bookingRepository
                .findByItemId(itemDto.getId(), Sort.by(Sort.Direction.DESC, "start"))
                .stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .map(BookingMapper::toItemBookingDto)
                .max(Comparator.comparing(BookingItemDto::getEnd))
                .orElse(null));
        itemDto.setNextBooking(bookingRepository
                .findByItemId(itemDto.getId(), Sort.by(Sort.Direction.ASC, "start"))
                .stream()
                .filter(booking -> !booking.getStatus().equals(Status.REJECTED))
                .map(BookingMapper::toItemBookingDto)
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .findFirst().orElse(null));
        return itemDto;
    }

    public ItemDto setComments(ItemDto itemDto) {
        itemDto.setComments(commentRepository
                .findByItemId(itemDto.getId()).stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
        return itemDto;
    }

}
