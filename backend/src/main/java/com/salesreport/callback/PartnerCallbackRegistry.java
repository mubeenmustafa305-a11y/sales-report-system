package com.salesreport.callback;

import com.salesreport.dto.PartnerCallbackRequestDto;
import com.salesreport.dto.PartnerDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * In-memory registry bridging the async partner callback to the workflow orchestrator.
 * Handles early callbacks that arrive before registration completes.
 */
@Slf4j
@Component
public class PartnerCallbackRegistry {

    private static final long CALLBACK_TIMEOUT_MINUTES = 5;

    private final Map<String, PendingCallback> pendingCallbacks = new ConcurrentHashMap<>();
    private final Map<String, PartnerDataDto> earlyCallbacks = new ConcurrentHashMap<>();

    public CompletableFuture<PartnerDataDto> register(String requestId, Long reportId) {
        PartnerDataDto early = earlyCallbacks.remove(requestId);
        if (early != null) {
            log.info("Completing early partner callback for requestId={}, reportId={}", requestId, reportId);
            return CompletableFuture.completedFuture(early);
        }

        CompletableFuture<PartnerDataDto> future = new CompletableFuture<>();
        pendingCallbacks.put(requestId, new PendingCallback(reportId, future));

        future.orTimeout(CALLBACK_TIMEOUT_MINUTES, TimeUnit.MINUTES)
                .whenComplete((result, error) -> pendingCallbacks.remove(requestId));

        log.info("Registered callback waiter for requestId={}, reportId={}", requestId, reportId);
        return future;
    }

    public void complete(PartnerCallbackRequestDto callback) {
        PendingCallback pending = pendingCallbacks.get(callback.getRequestId());
        if (pending == null) {
            log.info("Buffering early partner callback for requestId={}", callback.getRequestId());
            earlyCallbacks.put(callback.getRequestId(), callback.getPartnerData());
            return;
        }
        if (!pending.reportId().equals(callback.getReportId())) {
            log.warn("ReportId mismatch for requestId={}: expected={}, got={}",
                    callback.getRequestId(), pending.reportId(), callback.getReportId());
            pending.future().completeExceptionally(
                    new IllegalStateException("Report ID mismatch in partner callback"));
            return;
        }
        log.info("Completing partner callback for requestId={}, reportId={}",
                callback.getRequestId(), callback.getReportId());
        pending.future().complete(callback.getPartnerData());
    }

    private record PendingCallback(Long reportId, CompletableFuture<PartnerDataDto> future) {
    }
}
