package com.happydieting.dev.repository;

import com.happydieting.dev.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);

    Optional<UserModel> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}