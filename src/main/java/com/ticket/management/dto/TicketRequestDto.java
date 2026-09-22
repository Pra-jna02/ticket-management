package com.ticket.management.dto;

import com.ticket.management.entity.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TicketRequestDto {

    @NotBlank
    private String title;

    private String description;

    private Priority priority;

    private Long createdBy;
}
