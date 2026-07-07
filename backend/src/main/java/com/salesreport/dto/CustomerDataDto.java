package com.salesreport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDataDto {

    private String partnerId;
    private int totalCustomers;
    private List<SegmentDto> segments;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SegmentDto {
        private String segment;
        private int count;
        private double percentage;
    }
}
