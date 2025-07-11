package com.example.hibnb_project.data.repository;

import com.example.hibnb_project.data.entity.ImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImgRepository extends JpaRepository<ImgEntity, Integer> {
}
