package com.mms.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
@Setter
public class Member {
    private Integer memberId;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String password;
    private LocalDate joinDate;
    private String status; // active, inactive
    private Boolean isLeader;
    private Integer managerId;
    private LocalDateTime createdAt;

    // Additional fields for display
    private String managerName;

    // Constructor
    public Member() {
        this.joinDate = LocalDate.now();
        this.status = "active";
        this.isLeader = false;
    }
}