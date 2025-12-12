//package com.mms.controller;
////import com.mms.service.DashboardService;
//import com.mms.model.dto.MonthlySummaryDTO;
//import com.mms.service.MemberService;
//import com.mms.service.ReportService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.time.LocalDate;
//
//@Controller
//public class DashboardController {
//
////    @Autowired
////    private DashboardService service;
//
//    @GetMapping("/")
//    public String home() {
//        return "redirect:/dashboard";
//    }
//
////    @GetMapping("/dashboard")
////    public String dashboard(Model model) {
////
////        model.addAttribute("summary", service.getSummary());
////        model.addAttribute("mealChart", service.getMealChart());
////        model.addAttribute("expenseChart", service.getExpenseChart());
////
////        return "dashboard/dashboard"; // dashboard.html
////    }
//@GetMapping("/dashboard")
//public String dashboard(Model model) {
//
//
//    return "test"; // dashboard.html
//}
//
//    @Autowired
//    private MemberService memberService;
//
////    @Autowired
////    private ExpenseService expenseService;
////
////    @Autowired
////    private CollectionService collectionService;
////
////    @Autowired
////    private MealService mealService;
//
//    @Autowired
//    private ReportService reportService;
//
//    @GetMapping("/home")
//    public String home(Model model) {
//        LocalDate now = LocalDate.now();
//        int currentMonth = now.getMonthValue();
//        int currentYear = now.getYear();
//
//        // Get dashboard statistics
//        long activeMemberCount = memberService.getActiveMemberCount();
//        MonthlySummaryDTO summary = reportService.getMonthlySummary(currentMonth, currentYear);
//
//        model.addAttribute("activeMemberCount", activeMemberCount);
//        model.addAttribute("currentMonth", currentMonth);
//        model.addAttribute("currentYear", currentYear);
//        model.addAttribute("summary", summary != null ? summary : new MonthlySummaryDTO());
//
//        return "dashboard";
//    }
//}
