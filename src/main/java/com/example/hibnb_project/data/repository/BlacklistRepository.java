package com.example.hibnb_project.data.repository;

import com.example.hibnb_project.data.entity.BlacklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistRepository extends JpaRepository<BlacklistEntity, Integer> {
    BlacklistEntity findByUsername(String username);
}
