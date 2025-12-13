package com.mms.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DailyAttendanceDTO {
    private LocalDate date;
    private List<Integer> breakfastMembers;
    private List<Integer> lunchMembers;
    private List<Integer> dinnerMembers;
}
