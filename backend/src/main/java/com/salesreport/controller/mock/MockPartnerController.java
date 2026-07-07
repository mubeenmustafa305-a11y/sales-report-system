package com.salesreport.controller.mock;

import com.salesreport.dto.PartnerInitRequestDto;
import com.salesreport.dto.PartnerInitResponseDto;
import com.salesreport.service.PartnerCallbackScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Simulates an external Partner API that returns immediately and callbacks later.
 */
@Slf4j
@RestController
@RequestMapping("/mock/partner")
@RequiredArgsConstructor
public class MockPartnerController {

    private final PartnerCallbackScheduler callbackScheduler;

    @PostMapping
    public PartnerInitResponseDto initiate(@RequestBody PartnerInitRequestDto request) {
        String requestId = UUID.randomUUID().toString();
        Long reportId = request.getReportId();

        log.info("Partner API initiated: requestId={}, reportId={}", requestId, reportId);
        callbackScheduler.schedule(requestId, reportId);

        return PartnerInitResponseDto.builder()
                .requestId(requestId)
                .build();
    }
}
