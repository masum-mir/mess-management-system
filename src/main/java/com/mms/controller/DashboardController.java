package com.mms.controller;
//import com.mms.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

//    @Autowired
//    private DashboardService service;

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

//    @GetMapping("/dashboard")
//    public String dashboard(Model model) {
//
//        model.addAttribute("summary", service.getSummary());
//        model.addAttribute("mealChart", service.getMealChart());
//        model.addAttribute("expenseChart", service.getExpenseChart());
//
//        return "dashboard/dashboard"; // dashboard.html
//    }
@GetMapping("/dashboard")
public String dashboard(Model model) {


    return "test"; // dashboard.html
}
}
