package ru.practicum.shareit.booking.repo;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long booker_id, Sort sort);

    List<Booking> findAllByBookerIdAndStatus(Long booker_id, Status status, Sort sort);

    List<Booking> findAllByBookerIdAndEndBefore(Long booker_id, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByBookerIdAndStartAfter(Long booker_id, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long booker_id, LocalDateTime dateTime, LocalDateTime dateTime1, Sort sort);

    List<Booking> findByItemId(Long item_id, Sort sort);

    Boolean existsByBookerIdAndEndBeforeAndStatus(Long bookerId, LocalDateTime localDateTime, Status status);

    List<Booking> findAllByItemOwnerId(Long ownerId, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStatus(Long bookerId, Status status, Sort sort);

    List<Booking> findAllByItemOwnerIdAndEndBefore(Long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartAfter(Long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime dateTime,
                                                                LocalDateTime dateTime1, Sort sort);
}