package com.ticket.management.repository;

import com.ticket.management.entity.User;
import com.ticket.management.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findByRole(Role role);
}
