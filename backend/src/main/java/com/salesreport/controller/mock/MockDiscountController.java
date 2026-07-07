package com.salesreport.controller.mock;

import com.salesreport.dto.DiscountDataDto;
import com.salesreport.util.MockDataGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simulates an independent external Discount API.
 */
@RestController
@RequestMapping("/mock/discount")
@RequiredArgsConstructor
public class MockDiscountController {

    private final MockDataGenerator mockDataGenerator;

    @GetMapping
    public DiscountDataDto getDiscounts() {
        simulateLatency();
        return mockDataGenerator.generateDiscountData();
    }

    private void simulateLatency() {
        try {
            Thread.sleep(150 + (long) (Math.random() * 250));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
