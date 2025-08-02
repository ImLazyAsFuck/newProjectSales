package com.projectecommerce.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "BIT")
    private boolean status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    private String avatar;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true, length = 15)
    private String phone;

    @Column(columnDefinition = "BIT")
    private boolean isVerify;

    @Column(columnDefinition = "BIT")
    private boolean isLogin;

    private LocalDate createAt;

    private LocalDate updateAt;

    @Column(columnDefinition = "BIT")
    private boolean isDeleted;

    private LocalDate deletedAt;
}
