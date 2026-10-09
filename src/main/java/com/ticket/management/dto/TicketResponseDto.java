package com.ticket.management.dto;

import com.ticket.management.entity.enums.Priority;
import com.ticket.management.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Ticket response")
public class TicketResponseDto {

    @Schema(
            description = "Database generated ticket ID",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Unique ticket number",
            example = "TKT-1001"
    )
    private String ticketNumber;

    @Schema(
            description = "Ticket title",
            example = "Server Down"
    )
    private String title;

    @Schema(
            description = "Ticket description",
            example = "Production server is not responding"
    )
    private String description;

    @Schema(
            description = "Ticket priority",
            example = "HIGH"
    )
    private Priority priority;

    @Schema(
            description = "Current ticket status",
            example = "OPEN"
    )
    private Status status;

    @Schema(
            description = "Name of the ticket creator",
            example = "Prajna Nayak"
    )
    private String createdByName;

    @Schema(
            description = "Assigned support engineer",
            example = "Krishna"
    )
    private String assignedToName;

    @Schema(
            description = "Ticket creation timestamp"
    )
    private LocalDateTime createdDate;
}
