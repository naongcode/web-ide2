package com.example.myapp.Membership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.myapp.Membership.entity.User2;
import java.util.Optional;

public interface UserRepository2 extends JpaRepository<User2, String> {
    boolean existsByEmail(String email);
    Optional<User2> findByEmail(String email);

    boolean existsByUserId(String userId);
    Optional<User2> findByUserId(String userId);

}

