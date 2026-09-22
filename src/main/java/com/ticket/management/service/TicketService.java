package com.ticket.management.service;

import com.ticket.management.dto.TicketRequestDto;
import com.ticket.management.dto.TicketResponseDto;

import java.util.List;

public interface TicketService {

    TicketResponseDto createTicket(TicketRequestDto request);

    List<TicketResponseDto> getAllTickets();

    TicketResponseDto getTicketById(Long id);

    void deleteTicket(Long id);
}
