package com.salesreport.client;

import com.salesreport.dto.SalesDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

import static com.salesreport.config.AsyncConfig.WORKFLOW_EXECUTOR;

/**
 * HTTP client for the external Sales API.
 * Requires partnerId — invoked only after partner callback completes.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SalesApiClient {

    private final RestClient restClient;

    public SalesDataDto fetch(String partnerId) {
        log.info("Fetching sales data for partnerId={}", partnerId);
        return restClient.get()
                .uri("/mock/sales?partnerId={partnerId}", partnerId)
                .retrieve()
                .body(SalesDataDto.class);
    }

    @Async(WORKFLOW_EXECUTOR)
    public CompletableFuture<SalesDataDto> fetchAsync(String partnerId) {
        return CompletableFuture.completedFuture(fetch(partnerId));
    }
}
