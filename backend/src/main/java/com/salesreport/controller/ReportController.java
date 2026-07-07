package com.salesreport.controller;

import com.salesreport.dto.ReportResponseDto;
import com.salesreport.dto.ReportSummaryDto;
import com.salesreport.entity.Report;
import com.salesreport.service.ReportService;
import com.salesreport.workflow.WorkflowOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Thin REST layer — delegates all business logic to services and the workflow orchestrator.
 */
@Slf4j
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final WorkflowOrchestrator workflowOrchestrator;

    @PostMapping
    public ResponseEntity<ReportSummaryDto> generateReport() {
        Report report = reportService.createPendingReport();
        workflowOrchestrator.startGeneration(report.getId());
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ReportSummaryDto.builder()
                        .id(report.getId())
                        .status(report.getStatus().name())
                        .createdAt(report.getCreatedAt())
                        .build());
    }

    @GetMapping
    public ResponseEntity<List<ReportSummaryDto>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDto> getReport(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReport(id));
    }
}
