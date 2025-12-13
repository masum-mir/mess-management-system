package com.mms.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealShare {
    private Integer mealShareId;
    private Integer mealId;
    private Integer memberId;
    private BigDecimal shareAmount;
    private LocalDateTime calculatedAt;

}