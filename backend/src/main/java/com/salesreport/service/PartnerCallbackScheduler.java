package com.salesreport.service;

import com.salesreport.config.AppProperties;
import com.salesreport.dto.PartnerCallbackRequestDto;
import com.salesreport.dto.PartnerDataDto;
import com.salesreport.util.MockDataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Schedules delayed partner callbacks on the workflow executor.
 * Extracted from the mock controller so async execution is not blocked by self-invocation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerCallbackScheduler {

    private final MockDataGenerator mockDataGenerator;
    private final AppProperties appProperties;
    private final RestClient restClient;

    public void schedule(String requestId, Long reportId) {
        Thread.startVirtualThread(() -> {
            try {
                Thread.sleep(appProperties.getPartner().getCallbackDelayMs());
                PartnerDataDto partnerData = mockDataGenerator.generatePartnerData();

                PartnerCallbackRequestDto callback = PartnerCallbackRequestDto.builder()
                        .requestId(requestId)
                        .reportId(reportId)
                        .partnerData(partnerData)
                        .build();

                log.info("Sending partner callback for requestId={}, reportId={}", requestId, reportId);
                restClient.post()
                        .uri("/callback/partner")
                        .body(callback)
                        .retrieve()
                        .toBodilessEntity();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Partner callback interrupted for requestId={}", requestId, e);
            } catch (Exception e) {
                log.error("Failed to send partner callback for requestId={}", requestId, e);
            }
        });
    }
}
