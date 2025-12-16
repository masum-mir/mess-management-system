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
public class Expense {
    private Integer expenseId;
    private LocalDate expenseDate;
    private BigDecimal amount;
    private String description;
    private Integer categoryId;
    private Integer recordedBy;
    private LocalDateTime createdAt;

    // Additional fields for display
    private String categoryName;
    private String recordedByName;
//    private Category category;


    public Expense() {
        this.expenseDate = LocalDate.now();
    }
}