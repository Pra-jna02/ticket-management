package com.ticket.management.dto;

import com.ticket.management.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User response")
public class UserResponseDto {

    @Schema(description = "Database generated user id", example = "1")
    private Long id;

    @Schema(description = "Employee identifier", example = "EMP001")
    private String employeeId;

    @Schema(description = "Full name", example = "Prajna Nayak")
    private String name;

    @Schema(description = "Email address", example = "prajna.nayak@example.com")
    private String email;

    @Schema(description = "Department", example = "IT")
    private String department;

    @Schema(
            description = "User role",
            example = "SUPPORT_ENGINEER"
    )
    private Role role;
}
