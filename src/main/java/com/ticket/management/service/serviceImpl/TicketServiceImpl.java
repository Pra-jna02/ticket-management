package com.ticket.management.service.serviceImpl;

import com.ticket.management.dto.TicketRequestDto;
import com.ticket.management.dto.TicketResponseDto;
import com.ticket.management.entity.Ticket;
import com.ticket.management.entity.TicketHistory;
import com.ticket.management.entity.User;
import com.ticket.management.entity.enums.Priority;
import com.ticket.management.entity.enums.Role;
import com.ticket.management.entity.enums.Status;
import com.ticket.management.repository.TicketHistoryRepository;
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

    @Autowired
    private TicketHistoryRepository ticketHistoryRepository;

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

        return mapToResponse(savedTicket);
    }


    @Override
    public List<TicketResponseDto> getAllTickets() {

        List<Ticket> tickets = ticketRepository.findAll();

        List<TicketResponseDto> response = new ArrayList<>();

        for(Ticket ticket:tickets)
        {
            TicketResponseDto dto = mapToResponse(ticket);

            response.add(dto);
        }
        return response;
    }

    @Override
    public TicketResponseDto getTicketById(Long id) {

        Ticket ticket = getTicket(id);

        return mapToResponse(ticket);
    }

    @Override
    public TicketResponseDto updateTicket(Long id, TicketRequestDto request) {

        Ticket ticket = getTicket(id);

        User user = userRepository.findById(request.getCreatedBy())
                .orElseThrow(()->new RuntimeException("User not found"));

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setCreatedBy(user);

        Ticket updatedTicket = ticketRepository.save(ticket);

        return mapToResponse(updatedTicket);
    }

    @Override
    public void deleteTicket(Long id) {

        Ticket ticket = getTicket(id);

        ticketRepository.delete(ticket);
    }

    @Override
    public TicketResponseDto assignTicket(Long ticketId, Long userId) {

        Ticket ticket = getTicket(ticketId);

        User user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found"));

        if(user.getRole()!= Role.SUPPORT_ENGINEER){
            throw new RuntimeException("Only SUPPORT_ENGINEER can be assigned");
        }

        Status oldStatus = ticket.getStatus();

        ticket.setAssignedTo(user);
        ticket.setStatus(Status.ASSIGNED);

        Ticket updatedTicket = ticketRepository.save(ticket);

        //creating entry in TicketHistory Table
        createHistory(updatedTicket,oldStatus,Status.ASSIGNED,"Ticket assigned");

        return mapToResponse(updatedTicket);
    }

    @Override
    public TicketResponseDto changeStatus(Long ticketId, Status status) {

        Ticket ticket = getTicket(ticketId);

        //Checking the status is valid
        if(!isValidTransition(ticket.getStatus(),status))
        {
            throw new RuntimeException("Invalid status transition");
        }

        Status oldStatus = ticket.getStatus();

        ticket.setStatus(status);
        ticket.setUpdatedDate(LocalDateTime.now());

        Ticket updatedTicket = ticketRepository.save(ticket);

        createHistory(updatedTicket,oldStatus,status,"Status Changed");

        return mapToResponse(updatedTicket);
    }

    @Override
    public TicketResponseDto closeTicket(Long ticketId) {

        Ticket ticket = getTicket(ticketId);

        if (ticket.getStatus()!=Status.RESOLVED)
        {
            throw new RuntimeException("Ticket must be RESOLVED before closing");
        }

        Status oldStatus = ticket.getStatus();

        ticket.setStatus(Status.CLOSED);

        Ticket updatedTicket = ticketRepository.save(ticket);

        createHistory(updatedTicket,oldStatus,Status.CLOSED,"Ticket closed");

        return mapToResponse(updatedTicket);
    }

    @Override
    public TicketResponseDto reopenTicket(Long ticketId) {

        Ticket ticket = getTicket(ticketId);

        if(ticket.getStatus()!=Status.CLOSED)
        {
            throw new RuntimeException("Only CLOSED tickets can be reopened");
        }

        Status oldStatus = ticket.getStatus();

        ticket.setStatus(Status.REOPENED);

        Ticket updatedTicket = ticketRepository.save(ticket);

        createHistory(
                updatedTicket,
                oldStatus,
                Status.REOPENED,
                "Ticket reopened");

        return mapToResponse(updatedTicket);
    }

    @Override
    public TicketResponseDto resolveTicket(Long ticketId, String remarks) {

        Ticket ticket = getTicket(ticketId);

        if(ticket.getStatus()!=Status.IN_PROGRESS)
        {
            throw new RuntimeException("Only IN_PROGRESS tickets can be resolved");
        }

        if(ticket.getPriority()== Priority.CRITICAL && (remarks==null || remarks.isBlank()))
        {
            throw new RuntimeException("Critical tickets require remarks");
        }

        Status oldStatus = ticket.getStatus();

        ticket.setStatus(Status.RESOLVED);

        ticket.setResolvedDate(LocalDateTime.now());

        Ticket updatedTicket = ticketRepository.save(ticket);

        createHistory(updatedTicket,oldStatus,Status.RESOLVED,remarks);

        return mapToResponse(updatedTicket);
    }

    @Override
    public List<TicketHistory> getTicketHistory(Long ticketId) {
        return ticketHistoryRepository.findByTicketId(ticketId);
    }

    //Status validation method
    private boolean isValidTransition(Status currentStatus, Status newStatus)
    {
        return switch (currentStatus)
        {
            case OPEN, REOPENED -> newStatus == Status.ASSIGNED;

            case ASSIGNED -> newStatus == Status.IN_PROGRESS;

            case IN_PROGRESS -> newStatus == Status.RESOLVED;

            case RESOLVED -> newStatus == Status.CLOSED;

            case CLOSED -> newStatus == Status.REOPENED;

        };
    }

    //Entity - DTO Mapping
    private TicketResponseDto mapToResponse(Ticket ticket) {

        TicketResponseDto dto = new TicketResponseDto();

        if (ticket == null) {
            return dto;
        }

        dto.setId(ticket.getId());
        dto.setTicketNumber(ticket.getTicketNumber());
        dto.setTitle(ticket.getTitle());
        dto.setDescription(ticket.getDescription());
        dto.setPriority(ticket.getPriority());
        dto.setStatus(ticket.getStatus());

        if (ticket.getCreatedBy() != null) {

            dto.setCreatedByName(ticket.getCreatedBy().getName());
        }

        dto.setCreatedDate(ticket.getCreatedDate());

        return dto;
    }

    private Ticket getTicket(Long ticketId) {

        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    private void createHistory(Ticket ticket,Status oldStaus,Status newStatus,String remarks)
    {
        TicketHistory history = TicketHistory.builder()
                .ticket(ticket)
                .oldStatus(oldStaus)
                .newStatus(newStatus)
                .remarks(remarks)
                .changedDate(LocalDateTime.now())
                .build();

        ticketHistoryRepository.save(history);
    }
}


