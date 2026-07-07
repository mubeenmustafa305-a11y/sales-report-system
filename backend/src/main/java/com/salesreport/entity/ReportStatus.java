package com.salesreport.entity;

/**
 * Lifecycle states for asynchronous report generation.
 */
public enum ReportStatus {
    PENDING,
    PROCESSING,
    WAITING_CALLBACK,
    COLLECTING_DATA,
    COMPLETED,
    FAILED
}
