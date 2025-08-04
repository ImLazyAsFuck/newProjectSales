package com.projectecommerce.repository;

import com.projectecommerce.model.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Locale;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Page<User> findByIsDeletedFalse(Pageable pageable);
    Optional<User> findByIdAndIsDeletedFalse(Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByPhoneAndIdNot(String phone, Long id);

    Optional<User> findByUsernameAndPassword(@NotBlank(message = "Username cannot be blank") String username, @NotBlank(message = "Password cannot be blank") String password);
}
