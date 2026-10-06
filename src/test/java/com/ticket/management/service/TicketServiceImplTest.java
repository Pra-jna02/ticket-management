package com.ticket.management.service;

import com.ticket.management.dto.TicketRequestDto;
import com.ticket.management.dto.TicketResponseDto;
import com.ticket.management.entity.Ticket;
import com.ticket.management.entity.User;
import com.ticket.management.entity.enums.Priority;
import com.ticket.management.entity.enums.Role;
import com.ticket.management.entity.enums.Status;
import com.ticket.management.exception.InvalidOperationException;
import com.ticket.management.exception.ResourceNotFoundException;
import com.ticket.management.repository.TicketHistoryRepository;
import com.ticket.management.repository.TicketRepository;
import com.ticket.management.repository.UserRepository;
import com.ticket.management.service.serviceImpl.TicketServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketHistoryRepository ticketHistoryRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    void shouldCreateTicketSuccessfully() {

        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("Prajna");

        TicketRequestDto request = new TicketRequestDto();
        request.setTitle("Server Down");
        request.setDescription("Production issue");
        request.setPriority(Priority.HIGH);
        request.setCreatedBy(1L);

        Ticket savedTicket = new Ticket();
        savedTicket.setId(1L);
        savedTicket.setTitle("Server Down");
        savedTicket.setDescription("Production issue");
        savedTicket.setPriority(Priority.HIGH);
        savedTicket.setCreatedBy(user);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(savedTicket);

        // Act
        TicketResponseDto response =
                ticketService.createTicket(request);

        // Assert
        assertNotNull(response);
        assertEquals("Server Down", response.getTitle());
        assertEquals("Production issue",
                response.getDescription());
        assertEquals(Priority.HIGH,
                response.getPriority());

        verify(userRepository, times(1))
                .findById(1L);

        verify(ticketRepository, times(1))
                .save(any(Ticket.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        TicketRequestDto request =
                new TicketRequestDto();

        request.setTitle("Server Down");
        request.setDescription("Production issue");
        request.setPriority(Priority.HIGH);
        request.setCreatedBy(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> ticketService.createTicket(request));

        assertEquals(
                "User not found",
                exception.getMessage());

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }

    @Test
    void shouldGetTicketByIdSuccessfully()
    {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("Prajna");

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTicketNumber("TKT-1001");
        ticket.setTitle("Server Down");
        ticket.setDescription("Production server issue");
        ticket.setPriority(Priority.HIGH);
        ticket.setCreatedBy(user);

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        // Act
        TicketResponseDto response =
                ticketService.getTicketById(1L);

        // Assert
        assertNotNull(response);

        assertEquals(1L, response.getId());

        assertEquals("Server Down", response.getTitle());

        assertEquals("Production server issue", response.getDescription());

        assertEquals(Priority.HIGH, response.getPriority());

        assertEquals("Prajna", response.getCreatedByName());

        verify(ticketRepository, times(1))
                .findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTicketNotFound()
    {
        // Arrange
        when(ticketRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                        () -> ticketService.getTicketById(1L));

        assertEquals("Ticket not found", exception.getMessage());

        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void shouldAssignTicketSuccessfully() {

        // Arrange
        User engineer = new User();
        engineer.setId(2L);
        engineer.setName("Krishna");
        engineer.setRole(Role.SUPPORT_ENGINEER);

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setStatus(Status.OPEN);

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(engineer));

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(ticket);

        // Act
        TicketResponseDto response = ticketService.assignTicket(1L, 2L);

        // Assert
        assertNotNull(response);

        assertEquals(Status.ASSIGNED, ticket.getStatus());

        assertEquals(engineer, ticket.getAssignedTo());

        verify(ticketRepository, times(1))
                .findById(1L);

        verify(userRepository, times(1))
                .findById(2L);

        verify(ticketRepository, times(1))
                .save(any(Ticket.class));
    }

    @Test
    void shouldThrowExceptionWhenAssigningEmployee() {

        // Arrange
        User employee = new User();
        employee.setId(1L);
        employee.setName("Prajna");
        employee.setRole(Role.EMPLOYEE);

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setStatus(Status.OPEN);

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        // Act & Assert
        InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                        () -> ticketService.assignTicket(1L, 1L));

        assertEquals("Only SUPPORT_ENGINEER can be assigned", exception.getMessage());

        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void shouldThrowExceptionWhenTicketAlreadyAssigned() {

        User engineer = new User();
        engineer.setId(2L);
        engineer.setName("Krishna");
        engineer.setRole(Role.SUPPORT_ENGINEER);

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setAssignedTo(engineer);

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(engineer));

        InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                        () -> ticketService.assignTicket(1L, 2L));

        assertEquals(
                "Ticket is already assigned",
                exception.getMessage());

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }

    @Test
    void shouldRejectInvalidStatusTransition() {

        // Arrange
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setStatus(Status.OPEN);

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        // Act & Assert
        InvalidOperationException exception =
                assertThrows(
                        InvalidOperationException.class,
                        () -> ticketService.changeStatus(
                                1L,
                                Status.CLOSED));

        assertEquals(
                "Invalid status transition",
                exception.getMessage());

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }

    @Test
    void shouldResolveTicketSuccessfully() {

        // Arrange
        User engineer = new User();
        engineer.setId(2L);
        engineer.setName("Krishna");

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setPriority(Priority.HIGH);
        ticket.setStatus(Status.IN_PROGRESS);
        ticket.setAssignedTo(engineer);

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(ticket);

        // Act
        TicketResponseDto response =
                ticketService.resolveTicket(
                        1L,
                        "Issue fixed");

        // Assert
        assertNotNull(response);

        assertEquals(Status.RESOLVED, ticket.getStatus());

        verify(ticketRepository, times(1))
                .findById(1L);

        verify(ticketRepository, times(1))
                .save(any(Ticket.class));
    }

    @Test
    void shouldRejectCriticalTicketWithoutRemarks() {

        // Arrange
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setPriority(Priority.CRITICAL);
        ticket.setStatus(Status.IN_PROGRESS);

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        // Act & Assert
        InvalidOperationException exception = assertThrows(
                        InvalidOperationException.class,
                        () -> ticketService.resolveTicket(
                                1L,
                                ""));

        assertEquals(
                "Critical tickets require remarks",
                exception.getMessage());

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }

    @Test
    void shouldRejectCloseWhenNotResolved() {

        // Arrange
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setStatus(Status.IN_PROGRESS);

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        // Act & Assert
        InvalidOperationException exception = assertThrows(
                        InvalidOperationException.class,
                        () -> ticketService.closeTicket(1L));

        assertEquals(
                "Ticket must be RESOLVED before closing",
                exception.getMessage());

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }

    @Test
    void shouldCloseTicketSuccessfully() {

        // Arrange
        User engineer = new User();
        engineer.setId(2L);
        engineer.setName("Krishna");

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setStatus(Status.RESOLVED);
        ticket.setAssignedTo(engineer);

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(ticket);

        // Act
        TicketResponseDto response =
                ticketService.closeTicket(1L);

        // Assert
        assertNotNull(response);

        assertEquals(
                Status.CLOSED,
                ticket.getStatus());

        verify(ticketRepository, times(1))
                .findById(1L);

        verify(ticketRepository, times(1))
                .save(any(Ticket.class));
    }

    @Test
    void shouldRejectReopenWhenNotClosed() {

        // Arrange
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setStatus(Status.IN_PROGRESS);

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        // Act & Assert
        InvalidOperationException exception = assertThrows(
                        InvalidOperationException.class,
                        () -> ticketService.reopenTicket(1L));

        assertEquals(
                "Only CLOSED tickets can be reopened",
                exception.getMessage());

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }

    @Test
    void shouldReopenTicketSuccessfully() {

        // Arrange
        User engineer = new User();
        engineer.setId(2L);
        engineer.setName("Krishna");

        User creator = new User();
        creator.setId(1L);
        creator.setName("Prajna");

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setStatus(Status.CLOSED);
        ticket.setAssignedTo(engineer);
        ticket.setCreatedBy(creator);

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(ticket);

        // Act
        TicketResponseDto response = ticketService.reopenTicket(1L);

        // Assert
        assertNotNull(response);

        assertEquals(Status.REOPENED, ticket.getStatus());

        verify(ticketRepository, times(1))
                .findById(1L);

        verify(ticketRepository, times(1))
                .save(any(Ticket.class));
    }


    @Test
    void shouldSearchTicketsByStatus() {

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Server Down");
        ticket.setStatus(Status.OPEN);

        when(ticketRepository.findByStatus(Status.OPEN))
                .thenReturn(List.of(ticket));

        //Act
        List<TicketResponseDto> response = ticketService.searchTickets(Status.OPEN, null);

        assertNotNull(response);

        assertEquals(1, response.size());

        assertEquals(Status.OPEN, response.getFirst().getStatus());

        verify(ticketRepository, times(1))
                .findByStatus(Status.OPEN);
    }

    @Test
    void shouldSearchTicketsByPriority() {

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Production Issue");
        ticket.setPriority(Priority.HIGH);

        when(ticketRepository.findByPriority(Priority.HIGH))
                .thenReturn(List.of(ticket));

        //Act
        List<TicketResponseDto> response = ticketService.searchTickets(null, Priority.HIGH);

        assertNotNull(response);

        assertEquals(1, response.size());

        assertEquals(Priority.HIGH, response.getFirst().getPriority());

        verify(ticketRepository, times(1))
                .findByPriority(Priority.HIGH);
    }

    @Test
    void shouldReturnAllTicketsWhenNoFilterProvided() {

        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);

        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);

        when(ticketRepository.findAll())
                .thenReturn(List.of(ticket1, ticket2));

        //Act
        List<TicketResponseDto> response = ticketService.searchTickets(null, null);

        assertNotNull(response);

        assertEquals(2, response.size());

        verify(ticketRepository, times(1))
                .findAll();
    }

}
