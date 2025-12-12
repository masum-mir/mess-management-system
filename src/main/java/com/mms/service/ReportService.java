package com.mms.service;

import com.mms.model.dto.MemberReportDTO;
import com.mms.model.dto.MonthlySummaryDTO;
import com.mms.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public List<MemberReportDTO> getMemberReport(int month, int year) {
        return reportRepository.getMemberReport(month, year);
    }

    public MemberReportDTO getMemberReport(Integer memberId, int month, int year) {
        return reportRepository.getMemberReport(memberId, month, year);
    }

    public MemberReportDTO getMemberReportById(Integer memberId, int month, int year) {
        return reportRepository.getMemberReportById(memberId, month, year);
    }

    public MonthlySummaryDTO getMonthlySummary(int month, int year) {
        return reportRepository.getMonthlySummary(month, year);
    }

    public List<MemberReportDTO> getCurrentMonthMemberReport() {
        LocalDate now = LocalDate.now();
        return getMemberReport(now.getMonthValue(), now.getYear());
    }

    public MonthlySummaryDTO getCurrentMonthSummary() {
        LocalDate now = LocalDate.now();
        return getMonthlySummary(now.getMonthValue(), now.getYear());
    }
}