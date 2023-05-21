package ru.practicum.shareit.booking.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingEntryDto;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface BookingService {
    public BookingDto addBooking(Long id, BookingEntryDto bookingDto) throws ValidationException;
    public BookingDto approveBooking(Long id, Long bookingId, Boolean approved);
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long id,@PathVariable Long bookingId);
    public List<BookingDto> getAllBookingByState(Long id, String state, Integer type) throws Throwable;
}
