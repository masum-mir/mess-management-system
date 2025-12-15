package com.mms.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class Meal {
    private Integer mealId;
    private LocalDate mealDate;
    private Integer totalAttendances;
    private Integer memberId;
    private Integer createdBy;
    private LocalDateTime createdAt;

    public Meal() {
        this.mealDate = LocalDate.now();
        this.totalAttendances = 0;
    }
}