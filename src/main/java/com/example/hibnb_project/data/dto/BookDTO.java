package com.example.hibnb_project.data.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    private Integer id;
    private String username;
    private Integer accomid;
    private Integer person;
    private LocalDate checkindate;
    private LocalDate checkoutdate;
    private Integer totalPrice;
    private String status;
    private Boolean yesorno;
    private String payment;
    private String address;
    private String type;

//    private Set<ReportDTO> reports = new LinkedHashSet<>();
//    private Set<ReviewDTO> reviews = new LinkedHashSet<>();
}