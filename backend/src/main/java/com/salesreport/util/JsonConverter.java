package com.salesreport.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesreport.dto.AggregatedReportDataDto;
import com.salesreport.dto.CustomerDataDto;
import com.salesreport.dto.DiscountDataDto;
import com.salesreport.dto.PartnerDataDto;
import com.salesreport.dto.RevenueDataDto;
import com.salesreport.dto.SalesDataDto;
import org.springframework.stereotype.Component;

/**
 * Shared JSON serialization helper used by MapStruct mappers.
 */
@Component
public class JsonConverter {

    private final ObjectMapper objectMapper;

    public JsonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public AggregatedReportDataDto mapAggregatedData(String json) {
        return fromJson(json, AggregatedReportDataDto.class);
    }

    public PartnerDataDto mapPartnerData(String json) {
        return fromJson(json, PartnerDataDto.class);
    }

    public SalesDataDto mapSalesData(String json) {
        return fromJson(json, SalesDataDto.class);
    }

    public CustomerDataDto mapCustomerData(String json) {
        return fromJson(json, CustomerDataDto.class);
    }

    public RevenueDataDto mapRevenueData(String json) {
        return fromJson(json, RevenueDataDto.class);
    }

    public DiscountDataDto mapDiscountData(String json) {
        return fromJson(json, DiscountDataDto.class);
    }

    private <T> T fromJson(String json, Class<T> type) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize JSON", e);
        }
    }
}
