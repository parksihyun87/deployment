package com.example.hibnb_project.controller;

import com.example.hibnb_project.data.dto.ReportDTO;
import com.example.hibnb_project.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class ReportController {
    private final ReportService reportService;

    @GetMapping(value = "/report/findbyid")
    public ResponseEntity<?> findById(@RequestParam Integer reportid) {
        return ResponseEntity.ok(this.reportService.findById(reportid));
    }

    @GetMapping(value = "/report/findall")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(this.reportService.findAll());
    }

    @GetMapping(value = "/report/findbytype")
    public ResponseEntity<?> findByType(@RequestParam String type) {
        return ResponseEntity.ok(this.reportService.findByType(type));
    }

    @GetMapping(value = "/report/findbyaccomid")
    public ResponseEntity<?> findByAccomid(@RequestParam Integer accomid) {
        return ResponseEntity.ok(this.reportService.findByAccomid(accomid));
    }

    @PostMapping(value = "/report/save")
    public ResponseEntity<?> saveReport(@RequestBody ReportDTO reportDTO) {
        this.reportService.save(reportDTO);
        return ResponseEntity.ok("Report saved successfully");
    }

    @DeleteMapping(value = "/report/delete")
    public ResponseEntity<?> deleteReport(@RequestParam Integer reportid) {
        this.reportService.delete(reportid);
        return ResponseEntity.ok("Report deleted successfully");
    }

    @PutMapping(value="/report/updatestatus")
    public ResponseEntity<?> updateStatus(@RequestParam Integer reportid, @RequestParam String status) {
        try{
            reportService.updateStatus(reportid, status);
            return ResponseEntity.ok("Report updated successfully");
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
