package com.ticket.management.dto;

import com.ticket.management.entity.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketRequestDto {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Priority priority;

    @NotNull
    private Long createdBy;
}
