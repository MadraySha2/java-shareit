package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exceptions.DuplicateException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.practicum.shareit.item.ItemMapper.toItem;
import static ru.practicum.shareit.item.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.UserMapper.toUser;
import static ru.practicum.shareit.user.UserMapper.toUserDto;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    User user = new User();
    User user1 = new User();

    Item item = new Item();

    Item item1 = new Item();

    Booking booking = new Booking();

    Booking booking1 = new Booking();

    Booking booking2 = new Booking();

    PageRequest pageRequest = PageRequest.of(0, 10).withSort(Sort.by("start").descending());

    @BeforeEach
    public void beforeEach() throws DuplicateException {
        user = User.builder().id(1L).name("test").email("test@test.ru").build();
        user = toUser(userService.addUser(toUserDto(user)));
        item = Item.builder().id(1L).owner(user).description("Test").name("Test").available(true).build();
        item = toItem(itemService.addItem(1L, toItemDto(item)));
        user1 = User.builder().id(2L).name("test1").email("test1@test.ru").build();
        user1 = toUser(userService.addUser(toUserDto(user1)));
        item1 = Item.builder().id(2L).owner(user1).description("Test1").name("Test1").available(true).build();
        item1 = toItem(itemService.addItem(2L, toItemDto(item1)));
        booking = bookingRepository.save(Booking.builder().item(item).booker(user1).start(LocalDateTime.now().minusHours(1)).end(LocalDateTime.now().plusHours(1)).status(Status.WAITING).build());
        booking1 = bookingRepository.save(Booking.builder().item(item).booker(user1).start(LocalDateTime.now().minusHours(2)).end(LocalDateTime.now().minusHours(1)).status(Status.WAITING).build());
        booking2 = bookingRepository.save(Booking.builder().item(item).booker(user1).start(LocalDateTime.now().plusHours(1)).end(LocalDateTime.now().plusHours(2)).status(Status.WAITING).build());

    }

    @Test
    void addBooking() throws ValidationException {
        BookingEntryDto bookingEntryDto = BookingEntryDto.builder().itemId(item.getId()).start(LocalDateTime.now().minusHours(1)).end(LocalDateTime.now()).build();
        List<BookingDto> testBookings = bookingService.getAllBookingByState(2L, "WAITING", pageRequest);
        assertEquals(3, testBookings.size());
        bookingService.addBooking(2L, bookingEntryDto);
        List<BookingDto> testBookings1 = bookingService.getAllBookingByState(2L, "WAITING", pageRequest);
        assertEquals(4, testBookings1.size());
        assertEquals(3L, testBookings1.get(0).getId());
        assertEquals(4L, testBookings1.get(1).getId());
        assertEquals(1L, testBookings1.get(2).getId());
        assertEquals(2L, testBookings1.get(3).getId());
        assertTrue(testBookings1.get(0).getStart().isAfter(testBookings1.get(1).getStart()));
        assertTrue(testBookings1.get(1).getStart().isAfter(testBookings1.get(2).getStart()));
        assertTrue(testBookings1.get(2).getStart().isAfter(testBookings1.get(3).getStart()));
        assertTrue(testBookings1.get(3).getStart().isBefore(testBookings1.get(1).getStart()));
    }

    @Test
    void approveBooking() {
        bookingService.approveBooking(1L, 1L, true);
        bookingService.approveBooking(1L, 3L, false);
        List<BookingDto> testBookings = bookingService.getAllBookingByState(2L, "WAITING", pageRequest);
        assertEquals(1, testBookings.size());
        List<BookingDto> testBookingStatusRejected = bookingService.getAllBookingByState(2L, "REJECTED", pageRequest);
        assertEquals(1, testBookingStatusRejected.size());
        assertEquals(3L, testBookingStatusRejected.get(0).getId());
        List<BookingDto> testBookingStatusCurrent = bookingService.getAllBookingByState(2L, "CURRENT", pageRequest);
        assertEquals(1, testBookingStatusCurrent.size());
        assertEquals(1L, testBookingStatusCurrent.get(0).getId());


    }

    @Test
    void getBookingById() {
        BookingDto bookingDto = bookingService.getBookingById(1L, 1L);
        assertEquals(1L, bookingDto.getId());
        assertEquals(1L, bookingDto.getItem().getId());
        assertEquals("test1", bookingDto.getBooker().getName());
        assertEquals("test", bookingDto.getItem().getOwner().getName());
        assertEquals(Status.WAITING, bookingDto.getStatus());
        assertTrue(LocalDateTime.now().isBefore(bookingDto.getEnd()));
        assertTrue(LocalDateTime.now().isAfter(bookingDto.getStart()));
    }

    @Test
    void getAllBookingByState() {
        bookingService.approveBooking(1L, 1L, true);
        bookingService.approveBooking(1L, 3L, false);
        List<BookingDto> testBookings = bookingService.getAllBookingByState(2L, "WAITING", pageRequest);
        assertEquals(1, testBookings.size());
        List<BookingDto> testBookingStatusRejected = bookingService.getAllBookingByState(2L, "REJECTED", pageRequest);
        assertEquals(1, testBookingStatusRejected.size());
        assertEquals(3L, testBookingStatusRejected.get(0).getId());
        List<BookingDto> testBookingStatusCurrent = bookingService.getAllBookingByState(2L, "CURRENT", pageRequest);
        assertEquals(1, testBookingStatusCurrent.size());
        assertEquals(1L, testBookingStatusCurrent.get(0).getId());
        List<BookingDto> testBookingStatusPast = bookingService.getAllBookingByState(2L, "PAST", pageRequest);
        assertEquals(1, testBookingStatusPast.size());
        assertEquals(2L, testBookingStatusPast.get(0).getId());
        List<BookingDto> testBookingStatusFuture = bookingService.getAllBookingByState(2L, "FUTURE", pageRequest);
        assertEquals(1, testBookingStatusFuture.size());
        assertEquals(3L, testBookingStatusFuture.get(0).getId());
    }

    @Test
    void getAllOwnersBookingByState() {
        bookingService.approveBooking(1L, 1L, true);
        bookingService.approveBooking(1L, 3L, false);
        List<BookingDto> testBookings = bookingService.getAllOwnersBookingByState(1L, "WAITING", pageRequest);
        assertEquals(1, testBookings.size());
        assertEquals(2L, testBookings.get(0).getId());
        assertEquals(1L, testBookings.get(0).getItem().getOwner().getId());
        List<BookingDto> testBookingStatusRejected = bookingService.getAllOwnersBookingByState(1L, "REJECTED", pageRequest);
        assertEquals(1, testBookingStatusRejected.size());
        assertEquals(3L, testBookingStatusRejected.get(0).getId());
        assertEquals(1L, testBookingStatusRejected.get(0).getItem().getOwner().getId());
        List<BookingDto> testBookingStatusCurrent = bookingService.getAllOwnersBookingByState(1L, "CURRENT", pageRequest);
        assertEquals(1, testBookingStatusCurrent.size());
        assertEquals(1L, testBookingStatusCurrent.get(0).getId());
        assertEquals(1L, testBookingStatusCurrent.get(0).getItem().getOwner().getId());
        List<BookingDto> testBookingStatusPast = bookingService.getAllOwnersBookingByState(1L, "PAST", pageRequest);
        assertEquals(1, testBookingStatusPast.size());
        assertEquals(2L, testBookingStatusPast.get(0).getId());
        assertEquals(1L, testBookingStatusPast.get(0).getItem().getOwner().getId());
        List<BookingDto> testBookingStatusFuture = bookingService.getAllOwnersBookingByState(1L, "FUTURE", pageRequest);
        assertEquals(1, testBookingStatusFuture.size());
        assertEquals(3L, testBookingStatusFuture.get(0).getId());
        assertEquals(1L, testBookingStatusFuture.get(0).getItem().getOwner().getId());
    }
}