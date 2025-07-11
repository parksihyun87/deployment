package com.example.hibnb_project.data.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String username;
    private String password;
    private String role;
    private String name;
    private String phone;
    private String email;
    private Integer age;

//    private Set<AccomEntity> accoms = new LinkedHashSet<>();
//
//    private Set<BookEntity> books = new LinkedHashSet<>();
//
//    private Set<ReviewEntity> reviews = new LinkedHashSet<>();
}
