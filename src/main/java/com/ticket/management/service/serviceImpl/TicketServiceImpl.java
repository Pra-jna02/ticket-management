package com.ticket.management.service.serviceImpl;

import com.ticket.management.dto.TicketRequestDto;
import com.ticket.management.dto.TicketResponseDto;
import com.ticket.management.entity.Ticket;
import com.ticket.management.entity.User;
import com.ticket.management.entity.enums.Status;
import com.ticket.management.repository.TicketRepository;
import com.ticket.management.repository.UserRepository;
import com.ticket.management.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TicketResponseDto createTicket(TicketRequestDto request) {

        User user = userRepository.findById(request.getCreatedBy())
                .orElseThrow(()->new RuntimeException("User not found"));

        Ticket ticket = Ticket.builder()
                .ticketNumber("TKT-"+System.currentTimeMillis())
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(Status.OPEN)
                .createdBy(user)
                .createdDate(LocalDateTime.now())
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        TicketResponseDto dto = new TicketResponseDto();
        dto.setId(savedTicket.getId());
        dto.setTicketNumber(savedTicket.getTicketNumber());
        dto.setTitle(savedTicket.getTitle());
        dto.setDescription(savedTicket.getDescription());
        dto.setPriority(savedTicket.getPriority());
        dto.setStatus(savedTicket.getStatus());
        dto.setCreatedByName(user.getName());
        dto.setCreatedDate(savedTicket.getCreatedDate());

        return dto;
    }


    @Override
    public List<TicketResponseDto> getAllTickets() {

        List<Ticket> tickets = ticketRepository.findAll();

        List<TicketResponseDto> response = new ArrayList<>();

        for(Ticket ticket:tickets)
        {
            TicketResponseDto dto = new TicketResponseDto();
            dto.setId(ticket.getId());
            dto.setTicketNumber(ticket.getTicketNumber());
            dto.setTitle(ticket.getTitle());
            dto.setDescription(ticket.getDescription());
            dto.setPriority(ticket.getPriority());
            dto.setStatus(ticket.getStatus());
            dto.setCreatedByName(ticket.getCreatedBy().getName());
            dto.setCreatedDate(ticket.getCreatedDate());

            response.add(dto);
        }
        return response;
    }

    @Override
    public TicketResponseDto getTicketById(Long id) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Ticket not found"));

        TicketResponseDto dto = new TicketResponseDto();
        dto.setId(ticket.getId());
        dto.setTicketNumber(ticket.getTicketNumber());
        dto.setTitle(ticket.getTitle());
        dto.setDescription(ticket.getDescription());
        dto.setPriority(ticket.getPriority());
        dto.setStatus(ticket.getStatus());
        dto.setCreatedByName(ticket.getCreatedBy().getName());
        dto.setCreatedDate(ticket.getCreatedDate());

        return dto;
    }

    @Override
    public TicketResponseDto updateTicket(Long id, TicketRequestDto request) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Ticket not found"));

        User user = userRepository.findById(request.getCreatedBy())
                .orElseThrow(()->new RuntimeException("User not found"));

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setCreatedBy(user);

        Ticket updatedTicket = ticketRepository.save(ticket);

        TicketResponseDto dto = new TicketResponseDto();
        dto.setId(updatedTicket.getId());
        dto.setTicketNumber(updatedTicket.getTicketNumber());
        dto.setTitle(updatedTicket.getTitle());
        dto.setDescription(updatedTicket.getDescription());
        dto.setPriority(updatedTicket.getPriority());
        dto.setStatus(updatedTicket.getStatus());
        dto.setCreatedByName(updatedTicket.getCreatedBy().getName());
        dto.setCreatedDate(updatedTicket.getCreatedDate());

        return dto;
    }

    @Override
    public void deleteTicket(Long id) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));

        ticketRepository.delete(ticket);
    }
}
