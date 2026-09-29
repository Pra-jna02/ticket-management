package com.ticket.management.strategy;

import com.ticket.management.entity.User;
import com.ticket.management.entity.enums.Role;
import com.ticket.management.entity.enums.Status;
import com.ticket.management.exception.ResourceNotFoundException;
import com.ticket.management.repository.TicketRepository;
import com.ticket.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("leastLoadedStrategy")
public class LeastLoadedAssignmentStrategy implements TicketAssignmentStrategy{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public User assignEngineer() {

        List<User> engineers = userRepository.findByRole(Role.SUPPORT_ENGINEER);

        if(engineers.isEmpty())
        {
            throw new ResourceNotFoundException("No support engineers available");
        }

        List<Status> activeStatuses = List.of(
                        Status.ASSIGNED,
                        Status.IN_PROGRESS,
                        Status.REOPENED
                );
        User leastLoadedEngineer = engineers.getFirst();

        long minTickets = ticketRepository.countByAssignedToAndStatusIn(leastLoadedEngineer, activeStatuses);

        for(User engineer : engineers)
        {
            long ticketCount = ticketRepository.countByAssignedToAndStatusIn(engineer, activeStatuses);

            if(ticketCount < minTickets)
            {
                minTickets = ticketCount;
                leastLoadedEngineer = engineer;
            }
        }

        return leastLoadedEngineer;
    }
}
