package com.salesreport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDataDto {

    private String partnerId;
    private String partnerName;
    private String tier;
    private String region;
    private int activeContracts;
    private double commissionRate;
}
