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
public class DiscountDataDto {

    private BigDecimal totalDiscountAmount;
    private double averageDiscountPercent;
    private List<DiscountCategoryDto> categories;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiscountCategoryDto {
        private String category;
        private BigDecimal amount;
        private double percentOfTotal;
    }
}
