package com.portfolio.parkingmanagement.repository;

import com.portfolio.parkingmanagement.model.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Ticket findByCode(String code);
}
