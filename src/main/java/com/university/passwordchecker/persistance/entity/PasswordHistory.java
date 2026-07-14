package com.university.passwordchecker.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor@NoArgsConstructor
@Data
@Builder
@Table(name = "passwordHistory")
public class PasswordHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String generatedPassword;
    private LocalDateTime createdAt;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
}
