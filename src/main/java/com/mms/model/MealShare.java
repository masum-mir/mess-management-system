package com.mms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealShare {
    private Integer mealShareId;
    private Integer mealId;
    private Integer memberId;
    private BigDecimal shareAmount;
    private LocalDateTime calculatedAt;

    // Additional field for display
    private String memberName;
}