package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookingRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    public void testFindAllByBookerId() {
        User user = User.builder().name("TestUser").email("test@mail.com").build();
        entityManager.persist(user);
        Item item1 = Item.builder().owner(user).build();
        entityManager.persist(item1);
        User user1 = User.builder().name("TestUser1").email("test1@mail.com").build();
        entityManager.persist(user1);
        Booking booking1 = Booking.builder().booker(user).item(item1).build();
        Booking booking2 = Booking.builder().booker(user).item(item1).build();
        Booking booking3 = Booking.builder().booker(user1).item(item1).build();
        entityManager.persist(booking1);
        entityManager.persist(booking2);
        entityManager.persist(booking3);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> resultPage = bookingRepository.findAllByBookerId(user.getId(), pageable);

        assertThat(resultPage).isNotEmpty();
        assertThat(resultPage.getContent()).hasSize(2);
        assertThat(resultPage.getContent()).contains(booking1, booking2);
    }

    @Test
    public void testFindAllByBookerIdAndStatus() {
        User user = User.builder().name("TestUser").email("test@mail.com").build();
        entityManager.persist(user);
        Item item1 = Item.builder().owner(user).build();
        entityManager.persist(item1);
        User user1 = User.builder().name("TestUser1").email("test1@mail.com").build();
        entityManager.persist(user1);
        Status status = Status.APPROVED;
        Booking booking1 = Booking.builder().booker(user).item(item1).status(Status.APPROVED).build();
        Booking booking2 = Booking.builder().booker(user).item(item1).status(Status.APPROVED).build();
        Booking booking3 = Booking.builder().booker(user1).item(item1).status(Status.APPROVED).build();
        entityManager.persist(booking1);
        entityManager.persist(booking2);
        entityManager.persist(booking3);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> resultPage = bookingRepository.findAllByBookerIdAndStatus(user.getId(), status, pageable);

        assertThat(resultPage).isNotEmpty();
        assertThat(resultPage.getContent()).hasSize(2);
        assertThat(resultPage.getContent()).contains(booking1, booking2);
    }

    @Test
    public void testFindAllByBookerIdAndEndBefore() {
        User user = User.builder().name("TestUser").email("test@mail.com").build();
        entityManager.persist(user);
        Item item1 = Item.builder().owner(user).build();
        entityManager.persist(item1);
        User user1 = User.builder().name("TestUser1").email("test1@mail.com").build();
        entityManager.persist(user1);
        Booking booking1 = Booking.builder().booker(user).item(item1).start(LocalDateTime.now().minusHours(2)).end(LocalDateTime.now().minusHours(1)).build();
        Booking booking2 = Booking.builder().booker(user).item(item1).start(LocalDateTime.now().minusHours(2)).end(LocalDateTime.now().minusHours(1)).build();
        Booking booking3 = Booking.builder().booker(user1).item(item1).start(LocalDateTime.now().minusHours(2)).end(LocalDateTime.now().plusHours(1)).build();
        entityManager.persist(booking1);
        entityManager.persist(booking2);
        entityManager.persist(booking3);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> resultPage = bookingRepository.findAllByBookerIdAndEndBefore(user.getId(), LocalDateTime.now(), pageable);

        assertThat(resultPage).isNotEmpty();
        assertThat(resultPage.getContent()).hasSize(2);
        assertThat(resultPage.getContent()).contains(booking1, booking2);
    }

    @Test
    public void testFindAllByBookerIdAndStartAfter() {
        User user = User.builder().name("TestUser").email("test@mail.com").build();
        entityManager.persist(user);
        Item item1 = Item.builder().owner(user).build();
        entityManager.persist(item1);
        User user1 = User.builder().name("TestUser1").email("test1@mail.com").build();
        entityManager.persist(user1);
        Booking booking1 = Booking.builder().booker(user).item(item1).start(LocalDateTime.now().plusHours(2)).end(LocalDateTime.now().plusHours(1)).build();
        Booking booking2 = Booking.builder().booker(user).item(item1).start(LocalDateTime.now().plusHours(2)).end(LocalDateTime.now().plusHours(1)).build();
        Booking booking3 = Booking.builder().booker(user1).item(item1).start(LocalDateTime.now().minusHours(2)).end(LocalDateTime.now().plusHours(1)).build();
        entityManager.persist(booking1);
        entityManager.persist(booking2);
        entityManager.persist(booking3);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> resultPage = bookingRepository.findAllByBookerIdAndStartAfter(user.getId(), LocalDateTime.now(), pageable);

        assertThat(resultPage).isNotEmpty();
        assertThat(resultPage.getContent()).hasSize(2);
        assertThat(resultPage.getContent()).contains(booking1, booking2);
    }

    @Test
    public void testFindAllByBookerIdAndStartBeforeAndEndAfter() {
        User user = User.builder().name("TestUser").email("test@mail.com").build();
        entityManager.persist(user);
        Item item1 = Item.builder().owner(user).build();
        entityManager.persist(item1);
        User user1 = User.builder().name("TestUser1").email("test1@mail.com").build();
        entityManager.persist(user1);
        Booking booking1 = Booking.builder().booker(user).item(item1).start(LocalDateTime.now().plusHours(2)).end(LocalDateTime.now().plusHours(1)).build();
        Booking booking2 = Booking.builder().booker(user).item(item1).start(LocalDateTime.now().plusHours(2)).end(LocalDateTime.now().plusHours(1)).build();
        Booking booking3 = Booking.builder().booker(user).item(item1).start(LocalDateTime.now().minusHours(2)).end(LocalDateTime.now().plusHours(1)).build();
        entityManager.persist(booking1);
        entityManager.persist(booking2);
        entityManager.persist(booking3);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> resultPage = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(user.getId(), LocalDateTime.now(), LocalDateTime.now(), pageable);

        assertThat(resultPage).isNotEmpty();
        assertThat(resultPage.getContent()).hasSize(1);
        assertThat(resultPage.getContent()).contains(booking3);
    }

    @Test
    public void testFindAllByItemOwnerIdAndStatus() {
        User user = User.builder().name("TestUser").email("test@mail.com").build();
        entityManager.persist(user);
        Item item1 = Item.builder().owner(user).build();
        entityManager.persist(item1);
        User user1 = User.builder().name("TestUser1").email("test1@mail.com").build();
        entityManager.persist(user1);
        Status status = Status.APPROVED;
        Booking booking1 = Booking.builder()
                .booker(user).item(item1).status(Status.APPROVED).build();
        Booking booking2 = Booking.builder()
                .booker(user).item(item1).status(Status.APPROVED).build();
        Booking booking3 = Booking.builder()
                .booker(user1).status(Status.APPROVED).build();
        entityManager.persist(booking1);
        entityManager.persist(booking2);
        entityManager.persist(booking3);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> resultPage = bookingRepository
                .findAllByItemOwnerIdAndStatus(item1.getOwner().getId(), status, pageable);

        assertThat(resultPage).isNotEmpty();
        assertThat(resultPage.getContent()).hasSize(2);
        assertThat(resultPage.getContent()).contains(booking1, booking2);
    }

    @Test
    public void testFindAllByItemOwnerIdAndEndBefore() {
        User user = User.builder().name("TestUser").email("test@mail.com").build();
        entityManager.persist(user);
        Item item1 = Item.builder().owner(user).build();
        entityManager.persist(item1);
        User user1 = User.builder().name("TestUser1").email("test1@mail.com").build();
        entityManager.persist(user1);
        Booking booking1 = Booking.builder().booker(user)
                .item(item1).start(LocalDateTime.now().minusHours(2)).end(LocalDateTime.now().minusHours(1)).build();
        Booking booking2 = Booking.builder().booker(user)
                .item(item1).start(LocalDateTime.now().minusHours(2)).end(LocalDateTime.now().minusHours(1)).build();
        Booking booking3 = Booking.builder().booker(user1)
                .start(LocalDateTime.now().minusHours(2)).end(LocalDateTime.now().plusHours(1)).build();
        entityManager.persist(booking1);
        entityManager.persist(booking2);
        entityManager.persist(booking3);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> resultPage = bookingRepository
                .findAllByItemOwnerIdAndEndBefore(user.getId(), LocalDateTime.now(), pageable);

        assertThat(resultPage).isNotEmpty();
        assertThat(resultPage.getContent()).hasSize(2);
        assertThat(resultPage.getContent()).contains(booking1, booking2);
    }

    @Test
    public void testFindAllByItemOwnerIdAndStartAfter() {
        User user = User.builder().name("TestUser").email("test@mail.com").build();
        entityManager.persist(user);
        Item item1 = Item.builder().owner(user).build();
        entityManager.persist(item1);
        User user1 = User.builder().name("TestUser1").email("test1@mail.com").build();
        entityManager.persist(user1);
        Booking booking1 = Booking.builder().booker(user).item(item1).start(LocalDateTime.now()
                .plusHours(2)).end(LocalDateTime.now().plusHours(1)).build();
        Booking booking2 = Booking.builder().booker(user).item(item1).start(LocalDateTime.now()
                .plusHours(2)).end(LocalDateTime.now().plusHours(1)).build();
        Booking booking3 = Booking.builder().booker(user1).start(LocalDateTime.now()
                .minusHours(2)).end(LocalDateTime.now().plusHours(1)).build();
        entityManager.persist(booking1);
        entityManager.persist(booking2);
        entityManager.persist(booking3);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> resultPage = bookingRepository
                .findAllByItemOwnerIdAndStartAfter(user.getId(), LocalDateTime.now(), pageable);

        assertThat(resultPage).isNotEmpty();
        assertThat(resultPage.getContent()).hasSize(2);
        assertThat(resultPage.getContent()).contains(booking1, booking2);
    }

    @Test
    public void testFindAllByItemOwnerIdAndStartBeforeAndEndAfter() {
        User user = User.builder().name("TestUser").email("test@mail.com").build();
        entityManager.persist(user);
        Item item1 = Item.builder().owner(user).build();
        entityManager.persist(item1);
        User user1 = User.builder().name("TestUser1").email("test1@mail.com").build();
        entityManager.persist(user1);
        Booking booking1 = Booking.builder().booker(user).start(LocalDateTime.now()
                .plusHours(2)).end(LocalDateTime.now().plusHours(1)).build();
        Booking booking2 = Booking.builder().booker(user).start(LocalDateTime.now()
                .plusHours(2)).end(LocalDateTime.now().plusHours(1)).build();
        Booking booking3 = Booking.builder().booker(user).item(item1).start(LocalDateTime.now()
                .minusHours(2)).end(LocalDateTime.now().plusHours(1)).build();
        entityManager.persist(booking1);
        entityManager.persist(booking2);
        entityManager.persist(booking3);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> resultPage = bookingRepository
                .findAllByItemOwnerIdAndStartBeforeAndEndAfter(user.getId(), LocalDateTime.now(), LocalDateTime.now(), pageable);

        assertThat(resultPage).isNotEmpty();
        assertThat(resultPage.getContent()).hasSize(1);
        assertThat(resultPage.getContent()).contains(booking3);
    }

}