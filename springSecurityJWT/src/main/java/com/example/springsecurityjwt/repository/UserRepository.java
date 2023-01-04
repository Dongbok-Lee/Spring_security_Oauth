package com.example.springsecurityjwt.repository;

import com.example.springsecurityjwt.entity.User;
import com.example.springsecurityjwt.enumType.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByIdAndAuthProvider(String id, AuthProvider authProvider);
}

