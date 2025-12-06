package com.mms.model;

import java.sql.Date;
import java.sql.Timestamp;

    public class Member {

        private int memberId;
        private String name;
        private String roll;
        private String phone;
        private String email;
        private String address;
        private String password;
        private Date joinDate;
        private String status;
        private boolean isLeader;
        private Timestamp createdAt;

        // Getters and Setters

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRoll() {
            return roll;
        }

        public void setRoll(String roll) {
            this.roll = roll;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Date getJoinDate() {
            return joinDate;
        }

        public void setJoinDate(Date joinDate) {
            this.joinDate = joinDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isLeader() {
            return isLeader;
        }

        public void setLeader(boolean leader) {
            isLeader = leader;
        }

        public Timestamp getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
        }
    }
