package com.ticket.management.controller;

import com.ticket.management.dto.TicketHistoryResponseDto;
import com.ticket.management.dto.TicketRequestDto;
import com.ticket.management.dto.TicketResponseDto;
import com.ticket.management.entity.enums.Priority;
import com.ticket.management.entity.enums.Status;
import com.ticket.management.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@Tag(
        name = "Ticket Management",
        description = "APIs for managing tickets and ticket workflow"
)
public class TicketController {

    @Autowired
    private TicketService ticketService;


    @Operation(
            summary = "Create Ticket",
            description = "Creates a new support ticket"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Ticket created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @PostMapping
    public TicketResponseDto createTicket(@Valid @RequestBody TicketRequestDto request){

        return ticketService.createTicket(request);
    }


    @Operation(
            summary = "Get All Tickets",
            description = "Retrieves all tickets in the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tickets retrieved successfully"
            )
    })
    @GetMapping
    public List<TicketResponseDto> getAllTickets() {

        return ticketService.getAllTickets();
    }


    @Operation(
            summary = "Get Ticket By ID",
            description = "Retrieves ticket details using ticket ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ticket not found"
            )
    })
    @GetMapping("/{id}")
    public TicketResponseDto getTicketById(@PathVariable Long id) {

        return ticketService.getTicketById(id);
    }


    @Operation(
            summary = "Update Ticket",
            description = "Updates the details of an existing ticket"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ticket not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            )
    })
    @PutMapping("/{id}")
    public TicketResponseDto updateTicket(@PathVariable Long id,
                                          @Valid @RequestBody TicketRequestDto request) {

        return ticketService.updateTicket(id, request);
    }


    @Operation(
            summary = "Delete Ticket",
            description = "Deletes a ticket using the provided ticket ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Ticket deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ticket not found"
            )
    })
    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {

        ticketService.deleteTicket(id);
    }

    //Ticket History methods

    @Operation(
            summary = "Assign Ticket",
            description = "Assigns a ticket to a support engineer"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket assigned successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ticket or user not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid assignment operation"
            )
    })
    @PutMapping("/{ticketId}/assign/{userId}")
    public TicketResponseDto assignTicket(@PathVariable Long ticketId,
                                          @PathVariable Long userId){

        return ticketService.assignTicket(ticketId,userId);
    }


    @Operation(
            summary = "Auto Assign Ticket",
            description = "Automatically assigns a ticket using the configured assignment strategy"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket auto assigned successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ticket not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ticket already assigned"
            )
    })
    @PutMapping("/{ticketId}/auto-assign")
    public TicketResponseDto autoAssignTicket(@PathVariable Long ticketId)
    {
        return ticketService.autoAssignTicket(ticketId);

    }

    @PutMapping("/{ticketId}/status/{status}")
    public TicketResponseDto changeStatus(@PathVariable Long ticketId,
                                          @PathVariable Status status) {

        return ticketService.changeStatus(ticketId, status);
    }


    @Operation(
            summary = "Close Ticket",
            description = "Closes a resolved ticket"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket closed successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ticket must be resolved before closing"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ticket not found"
            )
    })
    @PutMapping("/{ticketId}/close")
    public TicketResponseDto closeTicket(@PathVariable Long ticketId) {

        return ticketService.closeTicket(ticketId);
    }


    @Operation(
            summary = "Reopen Ticket",
            description = "Reopens a previously closed ticket"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket reopened successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Only closed tickets can be reopened"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ticket not found"
            )
    })
    @PutMapping("/{ticketId}/reopen")
    public TicketResponseDto reopenTicket(@PathVariable Long ticketId) {

        return ticketService.reopenTicket(ticketId);
    }


    @Operation(
            summary = "Resolve Ticket",
            description = "Marks a ticket as resolved"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket resolved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid resolution operation"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ticket not found"
            )
    })
    @PutMapping("/{ticketId}/resolve")
    public TicketResponseDto resolveTicket(@PathVariable Long ticketId,
                                           @RequestParam(required = false) String remarks) {

        return ticketService.resolveTicket(ticketId, remarks);
    }


    @Operation(
            summary = "Get Ticket History",
            description = "Retrieves all history records for a ticket"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "History retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ticket not found"
            )
    })
    @GetMapping("/{ticketId}/history")
    public List<TicketHistoryResponseDto> getTicketHistory(@PathVariable Long ticketId)
    {
        return ticketService.getTicketHistory(ticketId);
    }


    @Operation(
            summary = "Search Tickets",
            description = "Search tickets by status or priority. If no filters are provided, all tickets are returned."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tickets retrieved successfully"
            )
    })
    @GetMapping("/search")
    public List<TicketResponseDto> searchTickets(@RequestParam(required = false) Status status,
                                                 @RequestParam(required = false) Priority priority)
    {

        return ticketService.searchTickets(status, priority);
    }
}
