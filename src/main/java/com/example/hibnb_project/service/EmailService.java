package com.example.hibnb_project.service;

import com.example.hibnb_project.data.entity.UserEntity;
import com.example.hibnb_project.data.entity.VerificationcodeEntity;
import com.example.hibnb_project.data.repository.UserRepository;
import com.example.hibnb_project.data.repository.VerificationcodeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final VerificationcodeRepository verificationcodeRepository;

    @Scheduled(fixedRate = 3600000) // 1시간마다 실행
    public void deleteExpiredCodes() {
        verificationcodeRepository.deleteAllByExpiresatBefore(LocalDateTime.now());
    }

    public void authenticationEmail(String email) { // 이메일 전송
        Optional<UserEntity> user = this.userRepository.findByEmail(email);
        if (user.isPresent()) {
            Random random = new Random();
            String verificationCode = String.format("%06d", random.nextInt(1000000)); //인증번호 만들기


            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Authentication Email");
            message.setText("Hello, your verificationCode is " + verificationCode + "!");
            message.setFrom("aoaaoadhkdwk@gmail.com"); // 발신자 이메일
            this.mailSender.send(message); // 이메일 보내기


            List<VerificationcodeEntity> verificationcodes = this.verificationcodeRepository.findByEmail(email);
            if (verificationcodes.size() > 0) {
                this.verificationcodeRepository.delete(verificationcodes.get(0)); // 기존 코드 있으면 삭제
            }
            this.verificationcodeRepository.save(VerificationcodeEntity.builder() // 새로운 코드 저장
                    .code(verificationCode)
                            .email(email)
                            .createdat(LocalDateTime.now())
                            .expiresat(LocalDateTime.now().plusSeconds(180))
                            .isverified(true)
                    .build());
            return;
        }
        throw new EntityNotFoundException("User not found");
    }
}
