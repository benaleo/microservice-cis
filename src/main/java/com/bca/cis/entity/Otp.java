package com.bca.cis.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "otp")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String otp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    private Boolean isValid = false;

    @Column(name = "phone")
    private String phone;

    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        expiredAt = createdAt.plusMinutes(5);
    }

}
