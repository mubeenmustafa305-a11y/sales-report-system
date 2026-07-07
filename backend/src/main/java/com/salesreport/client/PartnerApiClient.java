package com.salesreport.client;

import com.salesreport.dto.PartnerInitRequestDto;
import com.salesreport.dto.PartnerInitResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * HTTP client for the Partner API initiation endpoint.
 * Returns immediately with a requestId; actual data arrives via callback.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PartnerApiClient {

    private final RestClient restClient;

    public PartnerInitResponseDto initiate(Long reportId) {
        log.info("Initiating partner API for reportId={}", reportId);
        return restClient.post()
                .uri("/mock/partner")
                .body(new PartnerInitRequestDto(reportId))
                .retrieve()
                .body(PartnerInitResponseDto.class);
    }
}
