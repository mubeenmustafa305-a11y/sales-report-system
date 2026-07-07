package com.salesreport.controller;

import com.salesreport.dto.PartnerCallbackRequestDto;
import com.salesreport.callback.PartnerCallbackHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Receives asynchronous callbacks from the Partner API mock.
 */
@Slf4j
@RestController
@RequestMapping("/callback")
@RequiredArgsConstructor
public class PartnerCallbackController {

    private final PartnerCallbackHandler callbackHandler;

    @PostMapping("/partner")
    public ResponseEntity<Void> handlePartnerCallback(
            @Valid @RequestBody PartnerCallbackRequestDto request) {
        log.info("Received partner callback for reportId={}", request.getReportId());
        callbackHandler.handleCallback(request);
        return ResponseEntity.accepted().build();
    }
}
