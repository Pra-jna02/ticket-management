package com.ticket.management.dto;

import com.ticket.management.entity.enums.Role;
import lombok.Data;

@Data
public class UserResponseDto {

    private Long id;

    private String employeeId;

    private String name;

    private String email;

    private String department;

    private Role role;
}
