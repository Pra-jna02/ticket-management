package com.ticket.management.service.serviceImpl;

import com.ticket.management.dto.UserRequestDto;
import com.ticket.management.dto.UserResponseDto;
import com.ticket.management.entity.User;
import com.ticket.management.repository.UserRepository;
import com.ticket.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponseDto createUser(UserRequestDto request) {
        User user = User.builder()
                .employeeId(request.getEmployeeId())
                .name(request.getName())
                .email(request.getEmail())
                .department((request.getDepartment()))
                .role(request.getRole())
                .build();

        User saveUser = userRepository.save(user);

        UserResponseDto response = new UserResponseDto();
        response.setId(saveUser.getId());
        response.setEmployeeId(saveUser.getEmployeeId());
        response.setName(saveUser.getName());
        response.setEmail(saveUser.getEmail());
        response.setDepartment(saveUser.getDepartment());
        response.setRole(saveUser.getRole());

        return response;
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserResponseDto> responseList = new ArrayList<>();

        for(User user:users)
        {
            UserResponseDto dto = new UserResponseDto();
            dto.setId(user.getId());
            dto.setEmployeeId(user.getEmployeeId());
            dto.setName((user.getName()));
            dto.setEmail(user.getEmail());
            dto.setDepartment(user.getDepartment());
            dto.setRole(user.getRole());

            responseList.add(dto);
        }
        return responseList;
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException("User not found"));

        UserResponseDto dto = new UserResponseDto();

        dto.setId(user.getId());
        dto.setEmployeeId(user.getEmployeeId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setDepartment(user.getDepartment());
        dto.setRole(user.getRole());

        return dto;
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto request) {

        User user = userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found"));

        user.setEmployeeId(request.getEmployeeId());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setDepartment(request.getDepartment());
        user.setRole(request.getRole());

        User updatedUser = userRepository.save(user);

        UserResponseDto dto = new UserResponseDto();

        dto.setId(updatedUser.getId());
        dto.setEmployeeId(updatedUser.getEmployeeId());
        dto.setName(updatedUser.getName());
        dto.setEmail(updatedUser.getEmail());
        dto.setDepartment(updatedUser.getDepartment());
        dto.setRole(updatedUser.getRole());

        return dto;
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found"));

        userRepository.delete(user);
    }
}
