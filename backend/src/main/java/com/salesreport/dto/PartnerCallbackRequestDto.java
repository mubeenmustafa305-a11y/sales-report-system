package com.salesreport.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerCallbackRequestDto {

    @NotBlank
    private String requestId;

    @NotNull
    private Long reportId;

    @NotNull
    private PartnerDataDto partnerData;
}
