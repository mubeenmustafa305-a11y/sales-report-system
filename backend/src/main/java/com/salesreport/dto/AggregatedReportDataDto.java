package com.salesreport.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Final aggregated payload stored on the report and returned to the frontend.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AggregatedReportDataDto {

    private PartnerDataDto partner;
    private SalesDataDto sales;
    private CustomerDataDto customers;
    private RevenueDataDto revenue;
    private DiscountDataDto discounts;
    private SummaryDto summary;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SummaryDto {
        private BigDecimal totalRevenue;
        private int totalCustomers;
        private int totalSales;
        private BigDecimal totalDiscountAmount;
        private String topRegion;
        private String primaryPartnerName;
    }
}
