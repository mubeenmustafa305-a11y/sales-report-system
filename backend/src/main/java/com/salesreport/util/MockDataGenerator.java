package com.salesreport.util;

import com.salesreport.dto.CustomerDataDto;
import com.salesreport.dto.DiscountDataDto;
import com.salesreport.dto.PartnerDataDto;
import com.salesreport.dto.RevenueDataDto;
import com.salesreport.dto.SalesDataDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Generates realistic pseudo-random data for mock external APIs.
 */
@Component
public class MockDataGenerator {

    private static final String[] PARTNER_NAMES = {
            "Acme Distribution", "Global Trade Partners", "Nexus Commerce",
            "Summit Retail Group", "Vertex Supply Co"
    };
    private static final String[] TIERS = {"BRONZE", "SILVER", "GOLD", "PLATINUM"};
    private static final String[] REGIONS = {"North America", "Europe", "Asia Pacific", "Latin America"};
    private static final String[] SEGMENTS = {"Enterprise", "SMB", "Consumer", "Government"};
    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun"};
    private static final String[] DISCOUNT_CATEGORIES = {"Seasonal", "Loyalty", "Bulk", "Promotional"};

    private final Random random = new Random();

    public PartnerDataDto generatePartnerData() {
        return PartnerDataDto.builder()
                .partnerId("PTR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .partnerName(PARTNER_NAMES[random.nextInt(PARTNER_NAMES.length)])
                .tier(TIERS[random.nextInt(TIERS.length)])
                .region(REGIONS[random.nextInt(REGIONS.length)])
                .activeContracts(10 + random.nextInt(90))
                .commissionRate(round(5 + random.nextDouble() * 15, 2))
                .build();
    }

    public SalesDataDto generateSalesData(String partnerId) {
        List<SalesDataDto.RegionSalesDto> regions = new ArrayList<>();
        int totalOrders = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (String region : REGIONS) {
            int orders = 20 + random.nextInt(180);
            BigDecimal amount = BigDecimal.valueOf(5000 + random.nextInt(95000))
                    .setScale(2, RoundingMode.HALF_UP);
            regions.add(SalesDataDto.RegionSalesDto.builder()
                    .region(region)
                    .orders(orders)
                    .amount(amount)
                    .build());
            totalOrders += orders;
            totalAmount = totalAmount.add(amount);
        }

        return SalesDataDto.builder()
                .partnerId(partnerId)
                .totalOrders(totalOrders)
                .totalSalesAmount(totalAmount)
                .salesByRegion(regions)
                .build();
    }

    public CustomerDataDto generateCustomerData(String partnerId) {
        int total = 500 + random.nextInt(4500);
        List<CustomerDataDto.SegmentDto> segments = new ArrayList<>();
        int assigned = 0;

        for (int i = 0; i < SEGMENTS.length; i++) {
            int count = (i == SEGMENTS.length - 1)
                    ? total - assigned
                    : random.nextInt(total - assigned);
            assigned += count;
            double percentage = total > 0 ? round((double) count / total * 100, 1) : 0;
            segments.add(CustomerDataDto.SegmentDto.builder()
                    .segment(SEGMENTS[i])
                    .count(count)
                    .percentage(percentage)
                    .build());
        }

        return CustomerDataDto.builder()
                .partnerId(partnerId)
                .totalCustomers(total)
                .segments(segments)
                .build();
    }

    public RevenueDataDto generateRevenueData() {
        List<RevenueDataDto.MonthlyRevenueDto> monthly = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (String month : MONTHS) {
            BigDecimal revenue = BigDecimal.valueOf(80000 + random.nextInt(420000))
                    .setScale(2, RoundingMode.HALF_UP);
            monthly.add(RevenueDataDto.MonthlyRevenueDto.builder()
                    .month(month)
                    .revenue(revenue)
                    .build());
            total = total.add(revenue);
        }

        return RevenueDataDto.builder()
                .totalRevenue(total)
                .monthlyGrowthPercent(BigDecimal.valueOf(round(-5 + random.nextDouble() * 25, 1)))
                .monthlyRevenue(monthly)
                .build();
    }

    public DiscountDataDto generateDiscountData() {
        BigDecimal total = BigDecimal.valueOf(10000 + random.nextInt(90000))
                .setScale(2, RoundingMode.HALF_UP);
        List<DiscountDataDto.DiscountCategoryDto> categories = new ArrayList<>();
        BigDecimal allocated = BigDecimal.ZERO;

        for (int i = 0; i < DISCOUNT_CATEGORIES.length; i++) {
            BigDecimal amount = (i == DISCOUNT_CATEGORIES.length - 1)
                    ? total.subtract(allocated)
                    : total.multiply(BigDecimal.valueOf(0.15 + random.nextDouble() * 0.25))
                            .setScale(2, RoundingMode.HALF_UP);
            allocated = allocated.add(amount);
            double percent = total.compareTo(BigDecimal.ZERO) > 0
                    ? amount.divide(total, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100)).doubleValue()
                    : 0;
            categories.add(DiscountDataDto.DiscountCategoryDto.builder()
                    .category(DISCOUNT_CATEGORIES[i])
                    .amount(amount)
                    .percentOfTotal(round(percent, 1))
                    .build());
        }

        return DiscountDataDto.builder()
                .totalDiscountAmount(total)
                .averageDiscountPercent(round(3 + random.nextDouble() * 12, 1))
                .categories(categories)
                .build();
    }

    private double round(double value, int places) {
        return BigDecimal.valueOf(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }
}
