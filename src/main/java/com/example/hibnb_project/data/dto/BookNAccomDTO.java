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
public class BookNAccomDTO {
    private Integer id;
    private String username;
    private Integer accomid;
    private LocalDate checkindate;
    private LocalDate checkoutdate;
    private Integer totalPrice;
    private String status;
    private Boolean yesorno;
    private String payment;
    private String address;
    private String type;
    private AccomDTO accom;
}
