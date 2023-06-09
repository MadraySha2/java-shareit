package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRequestsRepository extends JpaRepository<ItemRequest, Long> {


    Page<ItemRequest> findByRequestorId(Long id, Pageable pageable);

    Page<ItemRequest> findAllByRequestorIdNot(Long id, Pageable pageable);
}
