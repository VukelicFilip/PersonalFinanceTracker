package com.example.personalfinancetracker.repository;

import com.example.personalfinancetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
