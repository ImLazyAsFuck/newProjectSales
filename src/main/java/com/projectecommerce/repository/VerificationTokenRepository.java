package com.projectecommerce.repository;

import com.projectecommerce.model.entity.User;
import com.projectecommerce.model.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByUser(User user);
    Optional<VerificationToken> findByToken(String token);
    void deleteByUser(User user);
}

