package com.ticket.management.strategy;

import com.ticket.management.entity.User;
import com.ticket.management.entity.enums.Role;
import com.ticket.management.exception.ResourceNotFoundException;
import com.ticket.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("roundRobinStrategy")
public class RoundRobinAssignmentStrategy implements TicketAssignmentStrategy{

    @Autowired
    private UserRepository userRepository;

    private static int currentIndex = 0;

    @Override
    public User assignEngineer() {
        List<User> engineers = userRepository.findByRole(Role.SUPPORT_ENGINEER);

        if(engineers.isEmpty())
        {
            throw new ResourceNotFoundException("No support engineers available");
        }

        User assignedEngineer = engineers.get(currentIndex);

        currentIndex = (currentIndex+1) % engineers.size();

        return assignedEngineer;
    }
}
