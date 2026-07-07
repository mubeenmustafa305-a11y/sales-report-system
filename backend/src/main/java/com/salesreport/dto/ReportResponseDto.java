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
public class ReportResponseDto {

    private Long id;
    private String status;
    private Instant createdAt;
    private Instant completedAt;
    private String errorMessage;
    private AggregatedReportDataDto aggregatedData;
    private PartnerDataDto partnerData;
    private SalesDataDto salesData;
    private CustomerDataDto customerData;
    private RevenueDataDto revenueData;
    private DiscountDataDto discountData;
}
