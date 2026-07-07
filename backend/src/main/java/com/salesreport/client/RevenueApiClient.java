package com.salesreport.client;

import com.salesreport.dto.RevenueDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

import static com.salesreport.config.AsyncConfig.WORKFLOW_EXECUTOR;

/**
 * HTTP client for the independent Revenue API.
 * Started immediately when workflow begins — no partner dependency.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RevenueApiClient {

    private final RestClient restClient;

    public RevenueDataDto fetch() {
        log.info("Fetching revenue data");
        return restClient.get()
                .uri("/mock/revenue")
                .retrieve()
                .body(RevenueDataDto.class);
    }

    @Async(WORKFLOW_EXECUTOR)
    public CompletableFuture<RevenueDataDto> fetchAsync() {
        return CompletableFuture.completedFuture(fetch());
    }
}
