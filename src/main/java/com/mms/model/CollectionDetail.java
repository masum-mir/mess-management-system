package com.mms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionDetail {
    private LocalDate collectDate;
    private BigDecimal amount;
    private String description;
}
