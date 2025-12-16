package com.mms.controller;

import com.mms.model.Member;
import com.mms.model.MonthlyBalanceReport;
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
import java.util.Optional;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private MemberService memberService;

    @GetMapping
    public String memberReport(@RequestParam(required = false) Integer month,
                               @RequestParam(required = false) Integer year,
                               Model model) {
        LocalDate now = LocalDate.now();

        if (month == null) month = now.getMonthValue();
        if (year == null) year = now.getYear();

        List<MonthlyBalanceReport> reports = reportService.getMemberReport(month, year);

        model.addAttribute("reports", reports);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);

        return "report/monthly-summary";
    }

    @GetMapping("/member/{id}")
    public String individualMemberReport(@PathVariable Integer id,
                                         @RequestParam(required = false) Integer month,
                                         @RequestParam(required = false) Integer year,
                                         Model model) {
        LocalDate now = LocalDate.now();
        int selectedMonth = (month != null) ? month : now.getMonthValue();
        int selectedYear = (year != null) ? year : now.getYear();

        MonthlyBalanceReport report = reportService.getMemberReport(id, selectedMonth, selectedYear);
        Optional<Member> member = memberService.getMemberById(id);

        model.addAttribute("report", report);
        if(member.isPresent()){
            model.addAttribute("member", member.get());
        } else {
            model.addAttribute("member", null);
        }

        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("pageTitle", "Individual Member Report");

        return "report/monthly-member-report";
    }
}

