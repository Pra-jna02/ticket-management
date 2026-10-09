package com.ticket.management.dto;

import com.ticket.management.entity.enums.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Ticket creation/update request")
public class TicketRequestDto {


    @Schema(
            description = "Title of the ticket",
            example = "Server Down"
    )
    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;


    @Schema(
            description = "Detailed description of the issue",
            example = "Production server is not responding"
    )
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;


    @Schema(
            description = "Priority of the ticket",
            example = "HIGH",
            allowableValues = {
                    "LOW",
                    "MEDIUM",
                    "HIGH",
                    "CRITICAL"
            }
    )
    @NotNull(message = "Priority is required")
    private Priority priority;


    @Schema(
            description = "ID of the user creating the ticket",
            example = "1"
    )
    @NotNull(message = "CreatedBy is required")
    private Long createdBy;
}
