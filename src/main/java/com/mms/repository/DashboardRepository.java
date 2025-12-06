//package com.mms.repository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Map;
//
//@Repository
//public class DashboardRepository {
//
//    @Autowired
//    private JdbcTemplate jdbc;
//
//    // Fetch total members
//    public int getTotalMembers() {
//        String sql = "SELECT COUNT(*) FROM members";
//        return jdbc.queryForObject(sql, Integer.class);
//    }
//
//    // Fetch total meals
//    public int getTotalMeals() {
//        String sql = "SELECT SUM(meal_count) FROM meals";
//        Integer result = jdbc.queryForObject(sql, Integer.class);
//        return result != null ? result : 0;
//    }
//
//    // Fetch total expenses
//    public double getTotalExpense() {
//        String sql = "SELECT SUM(amount) FROM expenses";
//        Double result = jdbc.queryForObject(sql, Double.class);
//        return result != null ? result : 0.0;
//    }
//
//    // Meal Rate = Total Expense / Total Meals
//    public double getMealRate() {
//        double totalMeals = getTotalMeals();
//        double totalExpense = getTotalExpense();
//
//        if (totalMeals == 0) return 0;
//        return totalExpense / totalMeals;
//    }
//
//    // Chart Data: Last 7 days meals
//    public List<Map<String, Object>> getDailyMeals() {
//        String sql = """
//            SELECT date, meal_count
//            FROM meals
//            ORDER BY date DESC
//            LIMIT 7
//        """;
//        return jdbc.queryForList(sql);
//    }
//
//    // Chart Data: Last 7 days expenses
//    public List<Map<String, Object>> getDailyExpenses() {
//        String sql = """
//            SELECT date, amount
//            FROM expenses
//            ORDER BY date DESC
//            LIMIT 7
//        """;
//        return jdbc.queryForList(sql);
//    }
//}
