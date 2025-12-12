package com.mms.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberReportDTO {
    private Integer memberId;
    private String memberName;
    private Integer month;
    private Integer year;
    private BigDecimal totalExpenses;
    private BigDecimal totalCollections;
    private BigDecimal mealCosts;
    private Integer totalMeals;
    private BigDecimal balance; // collections - (expenses + mealCosts)

    public void calculateBalance() {
        BigDecimal totalSpent = (totalExpenses != null ? totalExpenses : BigDecimal.ZERO)
                .add(mealCosts != null ? mealCosts : BigDecimal.ZERO);
        BigDecimal collected = totalCollections != null ? totalCollections : BigDecimal.ZERO;
        this.balance = collected.subtract(totalSpent);
    }
}