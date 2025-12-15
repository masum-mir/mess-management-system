package com.mms.model.dto;

import com.mms.model.Collection;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
public class MemberCollectionDto {
    private Integer memberId;
    private String memberName;
    private Integer transactionCount;
    private BigDecimal totalAmount;
    private List<Collection> transactions;
}
