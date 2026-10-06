package com.ticket.management.service;

import com.ticket.management.dto.UserRequestDto;
import com.ticket.management.dto.UserResponseDto;
import com.ticket.management.entity.User;
import com.ticket.management.entity.enums.Role;
import com.ticket.management.exception.ResourceNotFoundException;
import com.ticket.management.repository.UserRepository;
import com.ticket.management.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldCreateUserSuccessfully() {

        UserRequestDto request = new UserRequestDto();
        request.setEmployeeId("EMP001");
        request.setName("Prajna");
        request.setEmail("prajna@gmail.com");
        request.setDepartment("IT");
        request.setRole(Role.EMPLOYEE);

        User savedUser = User.builder()
                .id(1L)
                .employeeId("EMP001")
                .name("Prajna")
                .email("prajna@gmail.com")
                .department("IT")
                .role(Role.EMPLOYEE)
                .build();

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        //Act
        UserResponseDto response = userService.createUser(request);

        assertNotNull(response);

        assertEquals("EMP001", response.getEmployeeId());

        assertEquals("Prajna", response.getName());

        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void shouldGetAllUsersSuccessfully() {

        User user1 = User.builder()
                .id(1L)
                .employeeId("EMP001")
                .name("Prajna")
                .build();

        User user2 = User.builder()
                .id(2L)
                .employeeId("EMP002")
                .name("Krishna")
                .build();

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        //Act
        List<UserResponseDto> response = userService.getAllUsers();

        assertNotNull(response);

        assertEquals(2, response.size());

        verify(userRepository, times(1))
                .findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersExist() {

        when(userRepository.findAll())
                .thenReturn(Collections.emptyList());

        //Act
        List<UserResponseDto> response = userService.getAllUsers();

        assertNotNull(response);

        assertTrue(response.isEmpty());

        verify(userRepository, times(1))
                .findAll();
    }

    @Test
    void shouldGetUserByIdSuccessfully() {

        User user = User.builder()
                .id(1L)
                .employeeId("EMP001")
                .name("Prajna")
                .email("prajna@gmail.com")
                .department("IT")
                .role(Role.EMPLOYEE)
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        //Act
        UserResponseDto response = userService.getUserById(1L);

        assertNotNull(response);

        assertEquals(1L, response.getId());

        assertEquals("Prajna", response.getName());

        verify(userRepository, times(1))
                .findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                        () -> userService.getUserById(1L));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1))
                .findById(1L);
    }

    @Test
    void shouldUpdateUserSuccessfully() {

        User existingUser = User.builder()
                .id(1L)
                .employeeId("EMP001")
                .name("Prajna")
                .email("old@gmail.com")
                .department("IT")
                .role(Role.EMPLOYEE)
                .build();

        UserRequestDto request = new UserRequestDto();

        request.setEmployeeId("EMP001");
        request.setName("Prajna Nayak");
        request.setEmail("new@gmail.com");
        request.setDepartment("Support");
        request.setRole(Role.SUPPORT_ENGINEER);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.save(any(User.class)))
                .thenReturn(existingUser);

        UserResponseDto response =
                userService.updateUser(1L, request);

        assertNotNull(response);

        assertEquals("Prajna Nayak", existingUser.getName());

        assertEquals("new@gmail.com", existingUser.getEmail());

        assertEquals("Support", existingUser.getDepartment());

        assertEquals(Role.SUPPORT_ENGINEER, existingUser.getRole());

        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingUser() {

        UserRequestDto request = new UserRequestDto();

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,
                        () -> userService.updateUser(
                                1L,
                                request));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldDeleteUserSuccessfully() {

        User user = User.builder()
                .id(1L)
                .name("Prajna")
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        //Act
        userService.deleteUser(1L);

        verify(userRepository, times(1))
                .delete(user);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,
                        () -> userService.deleteUser(1L));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, never())
                .delete(any(User.class));
    }
}