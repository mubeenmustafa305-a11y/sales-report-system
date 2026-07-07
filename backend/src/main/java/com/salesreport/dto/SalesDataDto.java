package com.salesreport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesDataDto {

    private String partnerId;
    private int totalOrders;
    private BigDecimal totalSalesAmount;
    private List<RegionSalesDto> salesByRegion;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionSalesDto {
        private String region;
        private int orders;
        private BigDecimal amount;
    }
}
