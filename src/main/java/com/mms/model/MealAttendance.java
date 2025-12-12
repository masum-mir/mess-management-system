package com.mms.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MealAttendance {
    private Integer attendanceId;
    private Integer mealId;
    private Integer memberId;
    private Boolean isPresent;
    private Integer markedBy;
    private LocalDateTime markedAt;

    // Additional fields
    private String memberName;
    private String markedByName;
}