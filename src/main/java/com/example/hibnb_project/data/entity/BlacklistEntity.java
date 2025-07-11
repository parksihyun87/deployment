package com.example.hibnb_project.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "blacklist")
public class BlacklistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blackid", nullable = false)
    private Integer id;

    @NotNull
    @JoinColumn(name = "username", nullable = false)
    private String username;

    @Size(max = 45)
    @Column(name = "comment", length = 45)
    private String comment;

    @NotNull
    @Column(name = "addedAt", nullable = false, length = 45)
    private LocalDateTime addedAt;

}