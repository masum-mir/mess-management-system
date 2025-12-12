package com.mms.model;


import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Meal {
    private Integer mealId;
    private LocalDate mealDate;
    private String mealType;
    private BigDecimal cost;
    private String description;
    private Integer totalAttendees;
    private BigDecimal perPersonCost;
    private Integer month;
    private Integer year;
    private Integer createdBy;
    private LocalDateTime createdAt;

    // Additional fields
    private String createdByName;

    public Meal() {
        this.mealDate = LocalDate.now();
        this.cost = BigDecimal.ZERO;
        this.totalAttendees = 0;
        this.perPersonCost = BigDecimal.ZERO;
        this.month = LocalDate.now().getMonthValue();
        this.year = LocalDate.now().getYear();
    }
}