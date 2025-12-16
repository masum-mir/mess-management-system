package com.mms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberMonthlyReport {
    // Member Info
    private Long memberId;
    private String memberName;
    private String email;
    private String phone;

    // Period Info
    private int month;
    private int year;
    private String monthName;
    private Data reportDate;

    // Meal Details
    private Integer totalMeals;
    private BigDecimal perMealCost;
    private BigDecimal totalMealCost;

    // Collection Details
    private BigDecimal totalCollection;
    private List<CollectionDetail> collections;

    // Expense Details
    private BigDecimal sharedExpense;
    private BigDecimal totalExpense;

    // Balance
    private BigDecimal finalBalance;
    private String balanceStatus; // "DUE" or "ADVANCE"

    // Additional Statistics
    private Integer totalActiveDays;
    private BigDecimal averageMealsPerDay;
}

