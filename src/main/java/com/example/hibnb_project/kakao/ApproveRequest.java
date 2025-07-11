package com.example.hibnb_project.kakao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveRequest {
    private String tid;
    private String pg_token;
    private String username;
    private Integer bookid;
}