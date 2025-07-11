package com.example.hibnb_project.service;

import com.example.hibnb_project.data.dao.ReportDAO;
import com.example.hibnb_project.data.dto.ReportDTO;
import com.example.hibnb_project.data.entity.AccomEntity;
import com.example.hibnb_project.data.entity.BlacklistEntity;
import com.example.hibnb_project.data.entity.BookEntity;
import com.example.hibnb_project.data.entity.ReportEntity;
import com.example.hibnb_project.data.repository.AccomRepository;
import com.example.hibnb_project.data.repository.BlacklistRepository;
import com.example.hibnb_project.data.repository.BookRepository;
import com.example.hibnb_project.data.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportDAO reportDAO;
    private final ReportRepository reportRepository;
    private final BlacklistRepository blacklistRepository;
    private final AccomRepository accomRepository;
    private final BookRepository bookRepository;


    public ReportDTO findById(Integer reportid) {
        ReportEntity report = this.reportDAO.findById(reportid);
        return ReportDTO.builder()
                .id(report.getId())
                .accomid(report.getAccomid().getId())
                .bookid(report.getBookid().getId())
                .username(report.getUsername().getUsername())
                .type(report.getType())
                .comment(report.getComment())
                .createdAt(report.getCreatedAt())
                .status(report.getStatus())
                .build();
    }

    public List<ReportDTO> findAll() {
        List<ReportEntity> list = this.reportDAO.findAll();
        List<ReportDTO> dtos = new ArrayList<ReportDTO>();
        for (ReportEntity reportEntity : list) {
            ReportDTO reportDTO = ReportDTO.builder()
                    .id(reportEntity.getId())
                    .accomid(reportEntity.getAccomid().getId())
                    .bookid(reportEntity.getBookid().getId())
                    .username(reportEntity.getUsername().getUsername())
                    .type(reportEntity.getType())
                    .comment(reportEntity.getComment())
                    .createdAt(reportEntity.getCreatedAt())
                    .status(reportEntity.getStatus())
                    .build();
            dtos.add(reportDTO);
        }
        return dtos;
    }

    public List<ReportDTO> findByType(String type) {
        List<ReportEntity> list = this.reportDAO.findByType(type);
        List<ReportDTO> reportDTOList = new ArrayList<>();
        for (ReportEntity reportEntity : list) {
            ReportDTO reportDTO = ReportDTO.builder()
                    .id(reportEntity.getId())
                    .accomid(reportEntity.getAccomid().getId())
                    .bookid(reportEntity.getBookid().getId())
                    .username(reportEntity.getUsername().getUsername())
                    .type(reportEntity.getType())
                    .comment(reportEntity.getComment())
                    .createdAt(reportEntity.getCreatedAt())
                    .status(reportEntity.getStatus())
                    .build();
            reportDTOList.add(reportDTO);
        }
        return reportDTOList;
    }

    public List<ReportDTO> findByAccomid(Integer accomid) {
        List<ReportEntity> reportEntityList = this.reportDAO.findByAccomid(accomid);
        List<ReportDTO> reportDTOList = new ArrayList<>();
        for (ReportEntity reportEntity : reportEntityList) {
            ReportDTO reportDTO = ReportDTO.builder()
                    .id(reportEntity.getId())
                    .accomid(reportEntity.getAccomid().getId())
                    .bookid(reportEntity.getBookid().getId())
                    .username(reportEntity.getUsername().getUsername())
                    .type(reportEntity.getType())
                    .comment(reportEntity.getComment())
                    .createdAt(reportEntity.getCreatedAt())
                    .status(reportEntity.getStatus())
                    .build();
            reportDTOList.add(reportDTO);
        }
        return reportDTOList;
    }

    public void save(ReportDTO reportDTO) {
        this.reportDAO.save(reportDTO.getAccomid(), reportDTO.getBookid(), reportDTO.getUsername()
        , reportDTO.getType(), reportDTO.getComment(), reportDTO.getCreatedAt(), reportDTO.getStatus());
    }

    public void delete(Integer reportid) {
        this.reportDAO.delete(reportid);
    }

    private String findReportedUser(ReportEntity reportEntity) {
        // 숙소 id가 있으면 -> 해당 숙소의 호스트를 신고 대상으로 반환
        if (reportEntity.getAccomid() != null) {
            AccomEntity accomEntity = accomRepository.findById(reportEntity.getAccomid().getId())
                    .orElseThrow(() -> new IllegalArgumentException("숙소 없음"));
            return accomEntity.getHostid().getUsername();
        }

        // 예약 id가 있으면 -> 예약한 사용자 반환
        if (reportEntity.getBookid() != null) {
            BookEntity bookEntity = bookRepository.findById(reportEntity.getBookid().getId())
                    .orElseThrow(() -> new IllegalArgumentException("예약 없음"));
            return bookEntity.getUsername().getUsername();
        }

        // 둘 다 없으면 예외 처리
        throw new IllegalArgumentException("신고 대상 정보를 찾을 수 없습니다.");
    }

    public void updateStatus(Integer reportid, String status) {
        List<String> allowedStatus = List.of("PENDING", "APPROVED", "REJECTED");
        if (!allowedStatus.contains(status.toUpperCase())) {
            throw new IllegalArgumentException("NOT ALLOWED");
        }

        ReportEntity reportEntity = reportRepository.findById(reportid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신고"));

        reportEntity.setStatus(status.toUpperCase());
        reportRepository.save(reportEntity);

        if ("APPROVED".equalsIgnoreCase(status)) {
            try {
                String targetUsername = findReportedUser(reportEntity);

                BlacklistEntity bl = new BlacklistEntity();
                bl.setUsername(targetUsername);
                bl.setComment(reportEntity.getComment());
                bl.setAddedAt(LocalDateTime.now());

                blacklistRepository.save(bl);
            } catch (Exception e) {
                // 상세 로그 출력
                e.printStackTrace();
                throw new IllegalArgumentException("블랙리스트 저장 중 오류 발생: " + e.getMessage());
            }
        }
    }

}
