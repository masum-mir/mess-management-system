package com.mms.controller;

import com.mms.service.MemberService;
import com.mms.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class HomeController {

    private final MemberService memberService;
    private final ReportService reportService;

    public HomeController(MemberService memberService, ReportService reportService) {
        this.memberService = memberService;
        this.reportService = reportService;
    }

    @GetMapping("/")
    public String home(Model model) {
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();
//
//        // Get current month summary
//        MonthlySummaryDTO summary = reportService.getMonthlySummary(currentMonth, currentYear);
//
//        model.addAttribute("summary", summary);
//        model.addAttribute("activeMemberCount", memberService.getActiveMemberCount());
//        model.addAttribute("currentMonth", currentMonth);
//        model.addAttribute("currentYear", currentYear);
//        model.addAttribute("monthName", now.getMonth().toString());

        return "dashboard";
    }
}