package com.mms.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MealWithAttendanceDTO {
    private Integer mealId;
    private LocalDate mealDate;
    private String mealType;
    private String description;
    private Integer totalAttendances;
    private Integer presentCount;
    private Integer absentCount;
}
