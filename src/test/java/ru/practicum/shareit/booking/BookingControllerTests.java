package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    public void testAddBooking_InvalidDto() throws Exception {
        Long userId = 1L;
        BookingEntryDto bookingDto = BookingEntryDto.builder().build();
        mockMvc.perform(post("/bookings").header("X-Sharer-User-Id", userId).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(bookingDto))).andExpect(status().isBadRequest());
    }

    @Test
    public void testApproveBooking() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;

        BookingDto bookingResponse = BookingDto.builder().id(1L).item(Item.builder().id(1L).build()).booker(User.builder().id(1L).build()).start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(1)).status(Status.APPROVED).build();

        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingResponse);
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId).header("X-Sharer-User-Id", userId).param("approved", String.valueOf(approved))).andExpect(status().isOk()).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(bookingResponse.getId().intValue()))).andExpect(jsonPath("$.item.id", is(bookingResponse.getItem().getId().intValue()))).andExpect(jsonPath("$.booker.id", is(bookingResponse.getBooker().getId().intValue()))).andExpect(jsonPath("$.status", equalTo(bookingResponse.getStatus().toString())));

    }

    @Test
    public void testApproveBooking_InvalidUserId() throws Exception {
        Long invalidUserId = -1L;
        Long bookingId = 1L;
        Boolean approved = true;
        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean())).thenThrow(new NotFoundException("User not found."));
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId).header("X-Sharer-User-Id", invalidUserId).param("approved", String.valueOf(approved))).andExpect(status().isNotFound());
    }

    @Test
    public void testGetBookingById() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;
        BookingDto bookingResponse = BookingDto.builder().id(1L).item(Item.builder().id(1L).build()).booker(User.builder().id(1L).build()).start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(1)).status(Status.WAITING).build();
        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(bookingResponse);
        mockMvc.perform(get("/bookings/{bookingId}", bookingId).header("X-Sharer-User-Id", userId)).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(bookingResponse.getId().intValue()))).andExpect(jsonPath("$.item.id", is(bookingResponse.getItem().getId().intValue()))).andExpect(jsonPath("$.booker.id", is(bookingResponse.getBooker().getId().intValue()))).andExpect(jsonPath("$.status", equalTo(bookingResponse.getStatus().toString())));
    }

    @Test
    public void testGetBookingById_InvalidUserId() throws Exception {
        Long invalidUserId = -1L;
        Long bookingId = 1L;
        when(bookingService.getBookingById(anyLong(), anyLong())).thenThrow(new NotFoundException("User not found."));
        mockMvc.perform(get("/bookings/{bookingId}", bookingId).header("X-Sharer-User-Id", invalidUserId)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllBookingByState() throws Exception {
        Long userId = 1L;
        String state = "WAITING";
        int from = 0;
        int size = 10;
        BookingDto booking1 = BookingDto.builder().id(1L).item(Item.builder().id(1L).build()).booker(User.builder().id(1L).build()).start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(1)).status(Status.WAITING).build();
        List<BookingDto> bookingList = Collections.singletonList(booking1);
        when(bookingService.getAllBookingByState(anyLong(), anyString(), ArgumentMatchers.any(PageRequest.class))).thenReturn(bookingList);
        mockMvc.perform(get("/bookings").header("X-Sharer-User-Id", userId).param("state", state).param("from", String.valueOf(from)).param("size", String.valueOf(size))).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[0].item.id", is(booking1.getItem().getId().intValue()))).andExpect(jsonPath("$[0].booker.id", is(booking1.getBooker().getId().intValue())));

    }

    @Test
    public void testGetAllBookingByState_InvalidUserId() throws Exception {
        Long invalidUserId = -1L;
        String state = "ALL";
        int from = 0;
        int size = 10;

        when(bookingService.getAllBookingByState(anyLong(), anyString(), ArgumentMatchers.any(PageRequest.class))).thenThrow(new NotFoundException("User not found."));
        mockMvc.perform(get("/bookings").header("X-Sharer-User-Id", invalidUserId).param("state", state).param("from", String.valueOf(from)).param("size", String.valueOf(size))).andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllItemsBookings() throws Exception {
        Long userId = 1L;
        String state = "ALL";
        int from = 0;
        int size = 10;

        BookingDto booking1 = BookingDto.builder().id(1L).item(Item.builder().id(1L).build()).booker(User.builder().id(1L).build()).start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(1)).status(Status.WAITING).build();

        BookingDto booking2 = BookingDto.builder().id(2L).item(Item.builder().id(2L).build()).booker(User.builder().id(2L).build()).start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(1)).status(Status.APPROVED).build();

        List<BookingDto> bookingList = Arrays.asList(booking1, booking2);

        when(bookingService.getAllOwnersBookingByState(anyLong(), anyString(), ArgumentMatchers.any(PageRequest.class))).thenReturn(bookingList);

        mockMvc.perform(get("/bookings/owner").header("X-Sharer-User-Id", userId).param("state", state).param("from", String.valueOf(from)).param("size", String.valueOf(size))).andExpect(status().isOk()).andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[0].item.id", is(booking1.getItem().getId().intValue()))).andExpect(jsonPath("$[0].booker.id", is(booking1.getBooker().getId().intValue()))).andExpect(jsonPath("$[1].id", is(2))).andExpect(jsonPath("$[1].item.id", is(booking2.getItem().getId().intValue()))).andExpect(jsonPath("$[1].booker.id", is(booking2.getBooker().getId().intValue())));
    }

    @Test
    public void testGetAllItemsBookings_InvalidUserId() throws Exception {
        Long invalidUserId = -1L;
        String state = "ALL";
        int from = 0;
        int size = 10;

        when(bookingService.getAllOwnersBookingByState(anyLong(), anyString(), ArgumentMatchers.any(PageRequest.class))).thenThrow(new NotFoundException("User not found."));
        mockMvc.perform(get("/bookings/owner").header("X-Sharer-User-Id", invalidUserId).param("state", state).param("from", String.valueOf(from)).param("size", String.valueOf(size))).andExpect(status().isNotFound());
    }
}
