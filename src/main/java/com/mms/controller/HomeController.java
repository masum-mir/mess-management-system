package com.mms.controller;

import com.mms.model.Member;
import com.mms.service.MemberService;
import com.mms.service.ReportService;
import jakarta.servlet.http.HttpSession;
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
    public String home(HttpSession session, Model model) {
        LocalDate now = LocalDate.now();

        Member currentUser = (Member) session.getAttribute("loggedInMember");
        model.addAttribute("currentMember", currentUser);

        if (currentUser != null && currentUser.getIsManager() == true) {
            model.addAttribute("members", memberService.getAllMembers());
        }

        return "dashboard";
    }
}