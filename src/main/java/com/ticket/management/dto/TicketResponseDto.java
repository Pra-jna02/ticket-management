package com.ticket.management.dto;

import com.ticket.management.entity.enums.Priority;
import com.ticket.management.entity.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketResponseDto {

    private Long id;

    private String ticketNumber;

    private String title;

    private String description;

    private Priority priority;

    private Status status;

    private LocalDateTime createdDate;
}
