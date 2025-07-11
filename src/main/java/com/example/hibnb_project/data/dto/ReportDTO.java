package com.example.hibnb_project.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDTO {
    private Integer id;
    private Integer accomid;
    private Integer bookid;
    private String username;
    private String type;
    private String comment;
    private LocalDateTime createdAt;
    private String status;
}
