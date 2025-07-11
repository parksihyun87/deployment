package com.example.hibnb_project.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "verificationcode")
public class VerificationcodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codeid", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private UserEntity username;

    @Size(max = 6)
    @Column(name = "code", length = 6)
    private String code;

    @Column(name = "createdat")
    private LocalDateTime createdat;

    @Column(name = "expiresat")
    private LocalDateTime expiresat;

    @ColumnDefault("0")
    @Column(name = "isverified")
    private Boolean isverified;

}