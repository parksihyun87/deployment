package com.example.hibnb_project.controller;

import com.example.hibnb_project.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class EmailController {
    private final EmailService emailService;

    @GetMapping("/email")
    public String sendEmail(@RequestParam String email) {
        emailService.authenticationEmail(email);
        return "Email sent successfully!";


    }



}

