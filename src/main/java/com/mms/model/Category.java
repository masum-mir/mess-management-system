package com.mms.model;


import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Category {
    private Integer categoryId;
    private String categoryName;
    private String description;
    private Boolean isMealRelated;
    private Boolean isActive;

    public Category() {
        this.isMealRelated = false;
        this.isActive = true;
    }
}