package com.salesreport.callback;

import com.salesreport.dto.PartnerCallbackRequestDto;
import com.salesreport.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Handles inbound partner callbacks and resumes the waiting workflow.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerCallbackHandler {

    private final PartnerCallbackRegistry callbackRegistry;
    private final ReportService reportService;

    public void handleCallback(PartnerCallbackRequestDto request) {
        log.info("Processing partner callback for reportId={}, requestId={}",
                request.getReportId(), request.getRequestId());

        reportService.savePartnerData(request.getReportId(), request.getPartnerData());
        callbackRegistry.complete(request);
    }
}
