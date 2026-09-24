package com.ticket.management.controller;

import com.ticket.management.dto.TicketHistoryResponseDto;
import com.ticket.management.dto.TicketRequestDto;
import com.ticket.management.dto.TicketResponseDto;
import com.ticket.management.entity.TicketHistory;
import com.ticket.management.entity.enums.Priority;
import com.ticket.management.entity.enums.Status;
import com.ticket.management.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public TicketResponseDto createTicket(@Valid @RequestBody TicketRequestDto request){

        return ticketService.createTicket(request);
    }

    @GetMapping
    public List<TicketResponseDto> getAllTickets() {

        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public TicketResponseDto getTicketById(@PathVariable Long id) {

        return ticketService.getTicketById(id);
    }

    @PutMapping("/{id}")
    public TicketResponseDto updateTicket(@PathVariable Long id,
                                          @Valid @RequestBody TicketRequestDto request) {

        return ticketService.updateTicket(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {

        ticketService.deleteTicket(id);
    }

    //Ticket History methods

    @PutMapping("/{ticketId}/assign/{userId}")
    public TicketResponseDto assignTicket(@PathVariable Long ticketId,
                                          @PathVariable Long userId){

        return ticketService.assignTicket(ticketId,userId);
    }

    @PutMapping("/{ticketId}/status/{status}")
    public TicketResponseDto changeStatus(@PathVariable Long ticketId,
                                          @PathVariable Status status) {

        return ticketService.changeStatus(ticketId, status);
    }

    @PutMapping("/{ticketId}/close")
    public TicketResponseDto closeTicket(@PathVariable Long ticketId) {

        return ticketService.closeTicket(ticketId);
    }

    @PutMapping("/{ticketId}/reopen")
    public TicketResponseDto reopenTicket(@PathVariable Long ticketId) {

        return ticketService.reopenTicket(ticketId);
    }

    @PutMapping("/{ticketId}/resolve")
    public TicketResponseDto resolveTicket(@PathVariable Long ticketId,
                                           @RequestParam(required = false) String remarks) {

        return ticketService.resolveTicket(ticketId, remarks);
    }

    @GetMapping("/{ticketId}/history")
    public List<TicketHistoryResponseDto> getTicketHistory(@PathVariable Long ticketId)
    {
        return ticketService.getTicketHistory(ticketId);
    }

    @GetMapping("/search/status")
    public List<TicketResponseDto> getTicketByStatus(@RequestParam Status status)
    {
        return ticketService.getTicketByStatus(status);
    }

    @GetMapping("/search/priority")
    public List<TicketResponseDto> getTicketByPriority(@RequestParam Priority priority)
    {
        return ticketService.getTicketByPriority(priority);
    }
}
