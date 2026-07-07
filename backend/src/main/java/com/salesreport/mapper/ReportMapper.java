package com.salesreport.mapper;

import com.salesreport.dto.ReportResponseDto;
import com.salesreport.dto.ReportSummaryDto;
import com.salesreport.entity.Report;
import com.salesreport.util.JsonConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = JsonConverter.class)
public interface ReportMapper {

    @Mapping(target = "status", expression = "java(report.getStatus().name())")
    ReportResponseDto toResponse(Report report);

    List<ReportSummaryDto> toSummaryList(List<Report> reports);

    @Mapping(target = "status", expression = "java(report.getStatus().name())")
    ReportSummaryDto toSummary(Report report);
}
