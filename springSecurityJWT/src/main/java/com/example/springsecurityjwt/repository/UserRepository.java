package com.example.springsecurityjwt.repository;

import com.example.springsecurityjwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.security.AuthProvider;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByIdAndAuthProvider(String id, AuthProvider authProvider);
}

