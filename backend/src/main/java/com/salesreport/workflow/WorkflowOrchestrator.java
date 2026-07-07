package com.salesreport.workflow;

import com.salesreport.callback.PartnerCallbackRegistry;
import com.salesreport.client.CustomerApiClient;
import com.salesreport.client.DiscountApiClient;
import com.salesreport.client.PartnerApiClient;
import com.salesreport.client.RevenueApiClient;
import com.salesreport.client.SalesApiClient;
import com.salesreport.dto.AggregatedReportDataDto;
import com.salesreport.dto.CustomerDataDto;
import com.salesreport.dto.DiscountDataDto;
import com.salesreport.dto.PartnerDataDto;
import com.salesreport.dto.PartnerInitResponseDto;
import com.salesreport.dto.RevenueDataDto;
import com.salesreport.dto.SalesDataDto;
import com.salesreport.entity.ReportStatus;
import com.salesreport.service.ReportService;
import com.salesreport.util.ReportAggregator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static com.salesreport.config.AsyncConfig.WORKFLOW_EXECUTOR;

/**
 * Central orchestrator for the entire report generation lifecycle.
 *
 * <p>Workflow sequence:
 * <ol>
 *   <li>Mark report PROCESSING</li>
 *   <li>Fire Revenue + Discount APIs in parallel (independent)</li>
 *   <li>Initiate Partner API and wait for callback</li>
 *   <li>On callback: fire Sales + Customer APIs in parallel (partner-dependent)</li>
 *   <li>When all five data sources are ready: aggregate and mark COMPLETED</li>
 * </ol>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkflowOrchestrator {

    private final ReportService reportService;
    private final PartnerApiClient partnerApiClient;
    private final SalesApiClient salesApiClient;
    private final CustomerApiClient customerApiClient;
    private final RevenueApiClient revenueApiClient;
    private final DiscountApiClient discountApiClient;
    private final PartnerCallbackRegistry callbackRegistry;
    private final ReportAggregator reportAggregator;

    @Async(WORKFLOW_EXECUTOR)
    public void startGeneration(Long reportId) {
        log.info("Starting report generation workflow for reportId={}", reportId);
        try {
            reportService.updateStatus(reportId, ReportStatus.PROCESSING);

            // Independent APIs — start immediately while waiting for partner callback
            CompletableFuture<RevenueDataDto> revenueFuture = revenueApiClient.fetchAsync();
            CompletableFuture<DiscountDataDto> discountFuture = discountApiClient.fetchAsync();

            reportService.updateStatus(reportId, ReportStatus.WAITING_CALLBACK);
            PartnerInitResponseDto partnerInit = partnerApiClient.initiate(reportId);

            CompletableFuture<PartnerDataDto> partnerFuture =
                    callbackRegistry.register(partnerInit.getRequestId(), reportId);

            // Partner-dependent APIs — chained after callback arrives
            CompletableFuture<SalesDataDto> salesFuture = partnerFuture.thenCompose(partner -> {
                reportService.updateStatus(reportId, ReportStatus.COLLECTING_DATA);
                return salesApiClient.fetchAsync(partner.getPartnerId());
            });

            CompletableFuture<CustomerDataDto> customerFuture = partnerFuture.thenCompose(partner ->
                    customerApiClient.fetchAsync(partner.getPartnerId()));

            CompletableFuture.allOf(revenueFuture, discountFuture, partnerFuture, salesFuture, customerFuture)
                    .thenRun(() -> {
                        try {
                            PartnerDataDto partner = partnerFuture.join();
                            SalesDataDto sales = salesFuture.join();
                            CustomerDataDto customers = customerFuture.join();
                            RevenueDataDto revenue = revenueFuture.join();
                            DiscountDataDto discounts = discountFuture.join();

                            AggregatedReportDataDto aggregated = reportAggregator.aggregate(
                                    partner, sales, customers, revenue, discounts);

                            reportService.completeReport(
                                    reportId, partner, sales, customers, revenue, discounts, aggregated);
                        } catch (Exception e) {
                            reportService.failReport(reportId, e.getMessage());
                        }
                    })
                    .exceptionally(ex -> {
                        reportService.failReport(reportId, ex.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            log.error("Workflow failed for reportId={}", reportId, e);
            reportService.failReport(reportId, e.getMessage());
        }
    }
}
