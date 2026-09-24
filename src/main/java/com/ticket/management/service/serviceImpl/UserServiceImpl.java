package com.ticket.management.service.serviceImpl;

import com.ticket.management.dto.UserRequestDto;
import com.ticket.management.dto.UserResponseDto;
import com.ticket.management.entity.User;
import com.ticket.management.exception.ResourceNotFoundException;
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

       return mapToResponse(saveUser);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserResponseDto> responseList = new ArrayList<>();

        for(User user:users)
        {
            UserResponseDto dto = mapToResponse(user);

            responseList.add(dto);
        }
        return responseList;
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("User not found"));

        return mapToResponse(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto request) {

        User user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        user.setEmployeeId(request.getEmployeeId());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setDepartment(request.getDepartment());
        user.setRole(request.getRole());

        User updatedUser = userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
    }


    private UserResponseDto mapToResponse(User user) {

        UserResponseDto dto = new UserResponseDto();

        if (user == null) {
            return dto;
        }

        dto.setId(user.getId());
        dto.setEmployeeId(user.getEmployeeId());
        dto.setName((user.getName()));
        dto.setEmail(user.getEmail());
        dto.setDepartment(user.getDepartment());
        dto.setRole(user.getRole());

        return dto;
    }
}
