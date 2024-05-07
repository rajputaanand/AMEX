package com.amex.app.auth.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amex.app.auth.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

	
}
