package com.mms.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySummaryDTO {
    private Integer month;
    private Integer year;
    private BigDecimal totalExpenses;
    private BigDecimal totalCollections;
    private BigDecimal totalMealCosts;
    private Integer totalMealsServed;
    private Integer activeMembers;
    private BigDecimal balance; // collections - expenses

    public void calculateBalance() {
        BigDecimal collections = totalCollections != null ? totalCollections : BigDecimal.ZERO;
        BigDecimal expenses = totalExpenses != null ? totalExpenses : BigDecimal.ZERO;
        this.balance = collections.subtract(expenses);
    }
}