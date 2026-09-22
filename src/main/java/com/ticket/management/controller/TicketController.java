package com.ticket.management.controller;

import com.ticket.management.dto.TicketRequestDto;
import com.ticket.management.dto.TicketResponseDto;
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

}
