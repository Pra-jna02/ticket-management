package com.ticket.management.service;

import com.ticket.management.dto.TicketHistoryResponseDto;
import com.ticket.management.dto.TicketRequestDto;
import com.ticket.management.dto.TicketResponseDto;
import com.ticket.management.entity.enums.Priority;
import com.ticket.management.entity.enums.Status;

import java.util.List;

public interface TicketService {

    TicketResponseDto createTicket(TicketRequestDto request);

    List<TicketResponseDto> getAllTickets();

    TicketResponseDto getTicketById(Long id);

    TicketResponseDto updateTicket(Long id, TicketRequestDto request);

    void deleteTicket(Long id);

    //TicketHistory methods
    TicketResponseDto assignTicket(Long ticketId, Long userId);

    TicketResponseDto changeStatus(Long ticketId, Status status);

    TicketResponseDto closeTicket(Long ticketId);

    TicketResponseDto reopenTicket(Long ticketId);

    TicketResponseDto resolveTicket(Long ticketId, String remarks);

    List<TicketHistoryResponseDto> getTicketHistory(Long ticketId);


    //Search or filter functionality
    List<TicketResponseDto> getTicketByStatus(Status status);

    List<TicketResponseDto> getTicketByPriority(Priority priority);
}
