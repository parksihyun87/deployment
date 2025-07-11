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
public class AccomSeachDTO {
    private String address;
    private LocalDate checkindate;
    private LocalDate checkoutdate;
    private Integer maxcapacity;
}