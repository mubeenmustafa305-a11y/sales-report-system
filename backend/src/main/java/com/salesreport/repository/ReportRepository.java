package com.salesreport.repository;

import com.salesreport.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Persistence gateway for {@link Report} aggregates.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
