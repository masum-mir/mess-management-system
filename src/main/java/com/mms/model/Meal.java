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
    private String mealType;
    private Integer totalAttendances;
    private Integer month;
    private Integer year;
    private Integer createdBy;
    private LocalDateTime createdAt;

    public Meal() {
        this.mealDate = LocalDate.now();
        this.month = LocalDate.now().getMonthValue();
        this.year = LocalDate.now().getYear();
        this.totalAttendances = 0;
    }
}