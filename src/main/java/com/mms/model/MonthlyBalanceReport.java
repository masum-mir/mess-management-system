package com.mms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyBalanceReport {
    private Long memberId;
    private String name;
    private Integer mealsTaken;
    private BigDecimal perMealCost;
    private BigDecimal totalMealCost;
    private BigDecimal totalCollection;
    private BigDecimal perMemberShare;
    private BigDecimal finalBalance;
}