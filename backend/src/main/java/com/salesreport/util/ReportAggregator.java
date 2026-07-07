package com.salesreport.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesreport.dto.AggregatedReportDataDto;
import com.salesreport.dto.CustomerDataDto;
import com.salesreport.dto.DiscountDataDto;
import com.salesreport.dto.PartnerDataDto;
import com.salesreport.dto.RevenueDataDto;
import com.salesreport.dto.SalesDataDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Pure aggregation logic extracted from the orchestrator for testability and SRP.
 */
@Component
public class ReportAggregator {

    public AggregatedReportDataDto aggregate(
            PartnerDataDto partner,
            SalesDataDto sales,
            CustomerDataDto customers,
            RevenueDataDto revenue,
            DiscountDataDto discounts) {

        String topRegion = sales.getSalesByRegion().stream()
                .max(Comparator.comparing(SalesDataDto.RegionSalesDto::getAmount))
                .map(SalesDataDto.RegionSalesDto::getRegion)
                .orElse(null);

        AggregatedReportDataDto.SummaryDto summary = AggregatedReportDataDto.SummaryDto.builder()
                .totalRevenue(revenue.getTotalRevenue())
                .totalCustomers(customers.getTotalCustomers())
                .totalSales(sales.getTotalOrders())
                .totalDiscountAmount(discounts.getTotalDiscountAmount())
                .topRegion(topRegion)
                .primaryPartnerName(partner.getPartnerName())
                .build();

        return AggregatedReportDataDto.builder()
                .partner(partner)
                .sales(sales)
                .customers(customers)
                .revenue(revenue)
                .discounts(discounts)
                .summary(summary)
                .build();
    }
}
