package com.salesreport.client;

import com.salesreport.dto.DiscountDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

import static com.salesreport.config.AsyncConfig.WORKFLOW_EXECUTOR;

/**
 * HTTP client for the independent Discount API.
 * Started immediately when workflow begins — no partner dependency.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DiscountApiClient {

    private final RestClient restClient;

    public DiscountDataDto fetch() {
        log.info("Fetching discount data");
        return restClient.get()
                .uri("/mock/discount")
                .retrieve()
                .body(DiscountDataDto.class);
    }

    @Async(WORKFLOW_EXECUTOR)
    public CompletableFuture<DiscountDataDto> fetchAsync() {
        return CompletableFuture.completedFuture(fetch());
    }
}
