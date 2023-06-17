package shareitgateway.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareitgateway.booking.dto.BookItemRequestDto;
import shareitgateway.booking.dto.BookingState;
import shareitgateway.exceptions.NotSupportedStateException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;


    @Validated
    @PostMapping()
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid BookItemRequestDto requestDto) {
        return bookingClient.bookItem(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") Long id, @PathVariable Long bookingId, @RequestParam Boolean approved) {
        return bookingClient.approveBooking(id, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @Validated
    @GetMapping()
    public ResponseEntity<Object> getAllBookingByState(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(name = "state", defaultValue = "all") String stateParam, @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from, @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam).orElseThrow(() -> new NotSupportedStateException("Unknown state: " + stateParam));
        return bookingClient.getAllBookingByState(userId, state, from, size);
    }

    @Validated
    @GetMapping("/owner")
    public ResponseEntity<Object> getAllItemsBookings(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(name = "state", defaultValue = "all") String stateParam, @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from, @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam).orElseThrow(() -> new NotSupportedStateException("Unknown state: " + stateParam));
        return bookingClient.getAllOwnersBookingByState(userId, state, from, size);
    }

}
