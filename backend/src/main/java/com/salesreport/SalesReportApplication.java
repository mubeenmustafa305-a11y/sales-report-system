package com.salesreport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Entry point for the Sales Report Generation System.
 * {@link EnableAsync} activates Spring's async infrastructure used by the workflow orchestrator.
 */
@SpringBootApplication
@EnableAsync
public class SalesReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesReportApplication.class, args);
    }
}
