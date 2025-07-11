package com.example.hibnb_project.data.repository;

import com.example.hibnb_project.data.entity.AccomEntity;
import com.example.hibnb_project.data.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity,Integer> {
    List<ReportEntity> findByAccomid(AccomEntity accomid);

    List<ReportEntity> findByType(String type);
}
