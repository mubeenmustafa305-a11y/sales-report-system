package com.salesreport.controller.mock;

import com.salesreport.dto.CustomerDataDto;
import com.salesreport.util.MockDataGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simulates an external Customer API that requires a partnerId.
 */
@RestController
@RequestMapping("/mock/customer")
@RequiredArgsConstructor
public class MockCustomerController {

    private final MockDataGenerator mockDataGenerator;

    @GetMapping
    public CustomerDataDto getCustomers(@RequestParam String partnerId) {
        simulateLatency();
        return mockDataGenerator.generateCustomerData(partnerId);
    }

    private void simulateLatency() {
        try {
            Thread.sleep(200 + (long) (Math.random() * 300));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
