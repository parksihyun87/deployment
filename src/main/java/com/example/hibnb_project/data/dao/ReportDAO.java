package com.example.hibnb_project.data.dao;

import com.example.hibnb_project.data.entity.AccomEntity;
import com.example.hibnb_project.data.entity.BookEntity;
import com.example.hibnb_project.data.entity.ReportEntity;
import com.example.hibnb_project.data.entity.UserEntity;
import com.example.hibnb_project.data.repository.AccomRepository;
import com.example.hibnb_project.data.repository.BookRepository;
import com.example.hibnb_project.data.repository.ReportRepository;
import com.example.hibnb_project.data.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportDAO {
    private final ReportRepository reportRepository;
    private final AccomRepository accomRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ReportEntity findById(Integer id) {
        ReportEntity report = this.reportRepository.findById(id).orElse(null);
        if (report == null) {
            throw new EntityNotFoundException("Report not found");
        }
        return report;
    }

    public List<ReportEntity> findAll() {
        List<ReportEntity> reportEntityList = this.reportRepository.findAll();
        if (reportEntityList.isEmpty()) {
            return null;
        }
        return reportEntityList;
    }

    public List<ReportEntity> findByType(String type) {
        List<ReportEntity> reportEntityList = this.reportRepository.findByType(type);
        if (reportEntityList.isEmpty()) {
            return null;
        }
        return reportEntityList;
    }

    public List<ReportEntity> findByAccomid(Integer accommid) {
        AccomEntity accom = this.accomRepository.findById(accommid).orElse(null);
        if (accom == null) {
            throw new EntityNotFoundException("Accom not found");
        }
        List<ReportEntity> reportEntityList = this.reportRepository.findByAccomid(accom);
        if (reportEntityList.isEmpty()) {
            return null;
        }
        return reportEntityList;
    }

    public void save(Integer accommid, Integer bookid, String username, String type, String comment, LocalDateTime date, String status) {
        AccomEntity accom = this.accomRepository.findById(accommid).orElse(null);
        if (accom == null) {
            throw new EntityNotFoundException("Accom not found");
        }
        BookEntity book = this.bookRepository.findById(bookid).orElse(null);
        if (book == null) {
            throw new EntityNotFoundException("Book not found");
        }
        UserEntity user = this.userRepository.findById(username).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        ReportEntity reportEntity = ReportEntity.builder()
                .accomid(accom)
                .bookid(book)
                .username(user)
                .type(type)
                .comment(comment)
                .createdAt(date)
                .status(status)
                .build();
        this.reportRepository.save(reportEntity);
    }

    public void delete(Integer id) {
        if(this.reportRepository.existsById(id)){
            this.reportRepository.deleteById(id);
            return;
        }
        throw new EntityNotFoundException("Report not found");
    }



}
