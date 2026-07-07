package com.salesreport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportSummaryDto {

    private Long id;
    private String status;
    private Instant createdAt;
    private Instant completedAt;
    private String errorMessage;
}
