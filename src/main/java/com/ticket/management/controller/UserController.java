package com.ticket.management.controller;

import com.ticket.management.dto.UserRequestDto;
import com.ticket.management.dto.UserResponseDto;
import com.ticket.management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag( name = "User Management", description = "APIs for managing users")
public class UserController {

    @Autowired
    private UserService userService;


    @Operation(
            summary = "Create User",
            description = "Creates a new user in the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            )
    })
    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto request){

        return userService.createUser(request);
    }


    @Operation(
            summary = "Get All Users",
            description = "Fetches all users from the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully"
            )
    })
    @GetMapping
    public List<UserResponseDto> getAllUsers(){

        return userService.getAllUsers();
    }


    @Operation(
            summary = "Get User By ID",
            description = "Retrieves a user using the provided ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id){

        return userService.getUserById(id);
    }


    @Operation(
            summary = "Update User",
            description = "Updates an existing user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id,
                                      @Valid @RequestBody UserRequestDto request){

        return userService.updateUser(id, request);
    }


    @Operation(
            summary = "Delete User",
            description = "Deletes a user by ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "User deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){

        userService.deleteUser(id);
    }
}
