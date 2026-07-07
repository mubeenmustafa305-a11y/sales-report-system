package com.salesreport.client;

import com.salesreport.dto.CustomerDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

import static com.salesreport.config.AsyncConfig.WORKFLOW_EXECUTOR;

/**
 * HTTP client for the external Customer API.
 * Requires partnerId — invoked only after partner callback completes.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerApiClient {

    private final RestClient restClient;

    public CustomerDataDto fetch(String partnerId) {
        log.info("Fetching customer data for partnerId={}", partnerId);
        return restClient.get()
                .uri("/mock/customer?partnerId={partnerId}", partnerId)
                .retrieve()
                .body(CustomerDataDto.class);
    }

    @Async(WORKFLOW_EXECUTOR)
    public CompletableFuture<CustomerDataDto> fetchAsync(String partnerId) {
        return CompletableFuture.completedFuture(fetch(partnerId));
    }
}
