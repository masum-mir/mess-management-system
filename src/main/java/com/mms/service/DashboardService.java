//package com.mms.service;
//
//import com.mms.repository.DashboardRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service
//public class DashboardService {
//
//    @Autowired
//    private DashboardRepository repo;
//
//    public Map<String, Object> getSummary() {
//        Map<String, Object> summary = new HashMap<>();
//
//        summary.put("totalMembers", repo.getTotalMembers());
//        summary.put("totalMeals", repo.getTotalMeals());
//        summary.put("totalExpense", repo.getTotalExpense());
//        summary.put("mealRate", String.format("%.2f", repo.getMealRate()));
//
//        return summary;
//    }
//
//    // Prepare chart data for Thymeleaf
//    public Map<String, Object> getMealChart() {
//        List<Map<String, Object>> list = repo.getDailyMeals();
//
//        List<String> labels = new ArrayList<>();
//        List<Integer> data = new ArrayList<>();
//
//        for (int i = list.size() - 1; i >= 0; i--) {
//            labels.add(list.get(i).get("date").toString());
//            data.add(Integer.parseInt(list.get(i).get("meal_count").toString()));
//        }
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("labels", labels);
//        result.put("data", data);
//
//        return result;
//    }
//
//    public Map<String, Object> getExpenseChart() {
//        List<Map<String, Object>> list = repo.getDailyExpenses();
//
//        List<String> labels = new ArrayList<>();
//        List<Double> data = new ArrayList<>();
//
//        for (int i = list.size() - 1; i >= 0; i--) {
//            labels.add(list.get(i).get("date").toString());
//            data.add(Double.parseDouble(list.get(i).get("amount").toString()));
//        }
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("labels", labels);
//        result.put("data", data);
//
//        return result;
//    }
//}
