package com.mms.controller;

import com.mms.model.dto.MemberReportDTO;
import com.mms.model.dto.MonthlySummaryDTO;
import com.mms.service.MemberService;
import com.mms.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private MemberService memberService;

    @GetMapping("/monthly-summary")
    public String monthlySummary(@RequestParam(required = false) Integer month,
                                 @RequestParam(required = false) Integer year,
                                 Model model) {
        LocalDate now = LocalDate.now();

        if (month == null) month = now.getMonthValue();
        if (year == null) year = now.getYear();

        MonthlySummaryDTO summary = reportService.getMonthlySummary(month, year);

        model.addAttribute("summary", summary != null ? summary : new MonthlySummaryDTO());
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);

        return "report/monthly-summary";
    }

    @GetMapping("/member-report")
    public String memberReport(@RequestParam(required = false) Integer month,
                               @RequestParam(required = false) Integer year,
                               Model model) {
        LocalDate now = LocalDate.now();

        if (month == null) month = now.getMonthValue();
        if (year == null) year = now.getYear();

        List<MemberReportDTO> reports = reportService.getMemberReport(month, year);

        model.addAttribute("reports", reports);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);

        return "report/member-report";
    }

    @GetMapping("/meal-report")
    public String mealReport(@RequestParam(required = false) Integer month,
                             @RequestParam(required = false) Integer year,
                             Model model) {
        LocalDate now = LocalDate.now();

        if (month == null) month = now.getMonthValue();
        if (year == null) year = now.getYear();

        List<MemberReportDTO> reports = reportService.getMemberReport(month, year);

        model.addAttribute("reports", reports);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);

        return "report/meal-report";
    }

    @GetMapping("/member/{id}")
    public String individualMemberReport(@PathVariable Integer id,
                                         @RequestParam(required = false) Integer month,
                                         @RequestParam(required = false) Integer year,
                                         Model model) {
        LocalDate now = LocalDate.now();
        int selectedMonth = (month != null) ? month : now.getMonthValue();
        int selectedYear = (year != null) ? year : now.getYear();

        MemberReportDTO report = reportService.getMemberReport(id, selectedMonth, selectedYear);

        model.addAttribute("report", report);
        model.addAttribute("member", memberService.getMemberById(id));
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("pageTitle", "Individual Member Report");

        return "report/individual-report";
    }
}