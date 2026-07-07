package com.salesreport.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Type-safe binding for application-specific configuration.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String baseUrl = "http://localhost:8080";
    private Partner partner = new Partner();

    @Getter
    @Setter
    public static class Partner {
        private long callbackDelayMs = 4000;
    }
}
