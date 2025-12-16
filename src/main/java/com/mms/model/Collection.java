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
public class Collection {
    private Integer collectionId;
    private Integer memberId;
    private BigDecimal amount;
    private LocalDate collectDate;
    private String paymentMethod;
    private Integer collectedBy;
    private String remarks;
    private LocalDateTime createdAt;

    // Additional fields for display
    private String memberName;
    private String collectedByName;

    public Collection() {
        this.collectDate = LocalDate.now();
        this.paymentMethod = "cash";
    }
}