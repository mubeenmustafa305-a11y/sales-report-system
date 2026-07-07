package com.salesreport.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesreport.dto.AggregatedReportDataDto;
import com.salesreport.dto.CustomerDataDto;
import com.salesreport.dto.DiscountDataDto;
import com.salesreport.dto.PartnerDataDto;
import com.salesreport.dto.ReportResponseDto;
import com.salesreport.dto.ReportSummaryDto;
import com.salesreport.dto.RevenueDataDto;
import com.salesreport.dto.SalesDataDto;
import com.salesreport.entity.Report;
import com.salesreport.entity.ReportStatus;
import com.salesreport.exception.ResourceNotFoundException;
import com.salesreport.mapper.ReportMapper;
import com.salesreport.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Application service for report persistence and retrieval.
 * Contains no workflow orchestration logic — that belongs in {@link com.salesreport.workflow.WorkflowOrchestrator}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final ObjectMapper objectMapper;

    @Transactional
    public Report createPendingReport() {
        Report report = Report.builder()
                .status(ReportStatus.PENDING)
                .createdAt(Instant.now())
                .build();
        Report saved = reportRepository.save(report);
        log.info("Created report id={} with status=PENDING", saved.getId());
        return saved;
    }

    @Transactional(readOnly = true)
    public ReportResponseDto getReport(Long id) {
        return reportMapper.toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public List<ReportSummaryDto> getAllReports() {
        return reportMapper.toSummaryList(reportRepository.findAll());
    }

    @Transactional
    public void updateStatus(Long id, ReportStatus status) {
        Report report = findById(id);
        report.setStatus(status);
        reportRepository.save(report);
        log.info("Report id={} status updated to {}", id, status);
    }

    @Transactional
    public void savePartnerData(Long id, PartnerDataDto partnerData) {
        Report report = findById(id);
        report.setPartnerData(toJson(partnerData));
        reportRepository.save(report);
    }

    @Transactional
    public void completeReport(
            Long id,
            PartnerDataDto partner,
            SalesDataDto sales,
            CustomerDataDto customers,
            RevenueDataDto revenue,
            DiscountDataDto discounts,
            AggregatedReportDataDto aggregated) {

        Report report = findById(id);
        report.setPartnerData(toJson(partner));
        report.setSalesData(toJson(sales));
        report.setCustomerData(toJson(customers));
        report.setRevenueData(toJson(revenue));
        report.setDiscountData(toJson(discounts));
        report.setAggregatedData(toJson(aggregated));
        report.setStatus(ReportStatus.COMPLETED);
        report.setCompletedAt(Instant.now());
        reportRepository.save(report);
        log.info("Report id={} completed successfully", id);
    }

    @Transactional
    public void failReport(Long id, String errorMessage) {
        Report report = findById(id);
        report.setStatus(ReportStatus.FAILED);
        report.setErrorMessage(errorMessage);
        report.setCompletedAt(Instant.now());
        reportRepository.save(report);
        log.error("Report id={} failed: {}", id, errorMessage);
    }

    public Report findById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found: " + id));
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("JSON serialization failed", e);
        }
    }
}
