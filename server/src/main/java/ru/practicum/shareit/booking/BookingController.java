package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import javax.xml.bind.ValidationException;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping()
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long id,
                                 @RequestBody BookingEntryDto bookingDto) throws ValidationException {
        return bookingService.addBooking(id, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long id,
                                     @PathVariable Long bookingId, @RequestParam Boolean approved) {
        return bookingService.approveBooking(id, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long id, @PathVariable Long bookingId) {
        return bookingService.getBookingById(id, bookingId);
    }

    @GetMapping()
    public List<BookingDto> getAllBookingByState(@RequestHeader("X-Sharer-User-Id") Long id,
                                                 @RequestParam String state,
                                                 @RequestParam int from,
                                                 @RequestParam int size) throws ValidationException {

        return bookingService.getAllBookingByState(id, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllItemsBookings(@RequestHeader("X-Sharer-User-Id") Long id,
                                                @RequestParam String state,
                                                @RequestParam int from,
                                                @RequestParam int size) throws ValidationException {
        return bookingService.getAllOwnersBookingByState(id, state, from, size);
    }

}
