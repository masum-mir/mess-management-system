package com.mms.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportSummary {
    private int totalMembers;
    private int totalMeals;
    private BigDecimal totalCollection;
    private BigDecimal perMealCost;
    private int selectedMonth;
    private int selectedYear;
    private String monthName;
}
