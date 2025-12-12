package com.mms.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class ExpenseShare {
    private Integer shareId;
    private Integer expenseId;
    private Integer memberId;
    private BigDecimal shareAmount;
    private LocalDateTime shareDate;

    // Additional fields
    private String memberName;
}