package com.salesreport.controller.mock;

import com.salesreport.dto.RevenueDataDto;
import com.salesreport.util.MockDataGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simulates an independent external Revenue API.
 */
@RestController
@RequestMapping("/mock/revenue")
@RequiredArgsConstructor
public class MockRevenueController {

    private final MockDataGenerator mockDataGenerator;

    @GetMapping
    public RevenueDataDto getRevenue() {
        simulateLatency();
        return mockDataGenerator.generateRevenueData();
    }

    private void simulateLatency() {
        try {
            Thread.sleep(150 + (long) (Math.random() * 250));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
