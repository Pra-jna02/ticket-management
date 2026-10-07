package com.ticket.management.dto;

import com.ticket.management.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "User creation request")
public class UserRequestDto {

    @Schema(
            description = "Unique employee identifier",
            example = "EMP001"
    )
    @NotBlank(message = "Employee ID is required")
    private String employeeId;


    @Schema(
            description = "Full name of the user",
            example = "Prajna Nayak"
    )
    @NotBlank(message = "Name is required")
    private String name;


    @Schema(
            description = "Email address",
            example = "prajna@gmail.com"
    )
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;


    @Schema(
            description = "Department to which the user belongs",
            example = "IT"
    )
    @NotBlank(message = "Department is required ")
    private String department;


    @Schema(
            description = "Role of the user",
            example = "EMPLOYEE",
            allowableValues = {
                    "EMPLOYEE",
                    "SUPPORT_ENGINEER"
            }
    )
    @NotNull(message = "Role is required")
    private Role role;
}
