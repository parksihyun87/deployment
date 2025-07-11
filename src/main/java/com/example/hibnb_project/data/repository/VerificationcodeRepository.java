package com.example.hibnb_project.data.repository;

import com.example.hibnb_project.data.entity.VerificationcodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VerificationcodeRepository extends JpaRepository<VerificationcodeEntity, Long> {
    @Transactional
    void deleteAllByExpiresatBefore(LocalDateTime expiresat);

    List<VerificationcodeEntity> findByEmail(String email);
}
