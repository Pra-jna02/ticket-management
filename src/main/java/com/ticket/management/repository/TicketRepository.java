package com.ticket.management.repository;

import com.ticket.management.entity.Ticket;
import com.ticket.management.entity.enums.Priority;
import com.ticket.management.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    List<Ticket> findByStatus(Status status);

    List<Ticket> findByPriority(Priority priority);

}
