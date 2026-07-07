package com.salesreport.controller.mock;

import com.salesreport.dto.SalesDataDto;
import com.salesreport.util.MockDataGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simulates an external Sales API that requires a partnerId.
 */
@RestController
@RequestMapping("/mock/sales")
@RequiredArgsConstructor
public class MockSalesController {

    private final MockDataGenerator mockDataGenerator;

    @GetMapping
    public SalesDataDto getSales(@RequestParam String partnerId) {
        simulateLatency();
        return mockDataGenerator.generateSalesData(partnerId);
    }

    private void simulateLatency() {
        try {
            Thread.sleep(200 + (long) (Math.random() * 300));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
