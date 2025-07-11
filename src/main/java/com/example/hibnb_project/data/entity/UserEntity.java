package com.example.hibnb_project.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @Size(max = 100)
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 10)
    @NotNull
    @Column(name = "role", nullable = false, length = 10)
    private String role;

    @Size(max = 10)
    @NotNull
    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Size(max = 20)
    @NotNull
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "age", nullable = false)
    private Integer age;

    @OneToMany(mappedBy = "hostid")
    private Set<AccomEntity> accoms = new LinkedHashSet<>();

    @OneToMany(mappedBy = "username")
    private Set<BookEntity> books = new LinkedHashSet<>();

    @OneToMany(mappedBy = "username")
    private Set<ReportEntity> reports = new LinkedHashSet<>();

    @OneToMany(mappedBy = "username")
    private Set<ReviewEntity> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "username")
    private Set<VerificationcodeEntity> verificationcodes = new LinkedHashSet<>();


}