package com.ticket.management.dto;

import com.ticket.management.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotBlank
    private String employeeId;

    @NotBlank
    private String name;

    @Email
    private String email;

    private String department;

    private Role role;
}
