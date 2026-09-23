package com.ticket.management.dto;

import com.ticket.management.entity.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketHistoryResponseDto {

    private Long id;

    private Status oldStatus;

    private Status newStatus;

    private String changedBy;

    private String remarks;

    private LocalDateTime changedDate;
}