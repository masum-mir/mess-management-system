package com.mms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyBalanceReport {
    private Integer memberId;
    private String name;
    private Integer mealsTaken;
    private LocalDate reportDate;
    private BigDecimal perMealCost;
    private BigDecimal totalMealCost;
    private BigDecimal totalCollection;
    private BigDecimal perMemberShare;
    private BigDecimal finalBalance;
}