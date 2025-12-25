# Mess Management System

A comprehensive web-based application designed to digitize and streamline daily mess operations, including meal tracking, expense management, and financial record-keeping.

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Database Schema](#-database-schema)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Usage](#-usage)
- [Screenshots](#-screenshots) 

## 🎯 Overview

The Mess Management System is a digital solution that eliminates manual paperwork and calculation errors in mess management. It provides an organized platform for mess managers and members to track daily meals, monitor expenses, and maintain accurate financial records with complete transparency.

### Problem Statement

Traditional mess management relies on manual calculations, which often leads to:
- Errors in meal counting and expense tracking
- Time-consuming month-end calculations
- Confusion in deposit records
- Lack of transparency between managers and members

This system automates these processes, ensuring accuracy, efficiency, and reliability.

## ✨ Features

### Member Management
- Add and manage member profiles
- Track member status (active/inactive)
- Assign manager roles
- Store contact information and credentials

### Payment Collection
- Record all financial deposits
- Track payment methods
- Monitor collection dates
- Add remarks for transactions
- Identify payment collectors

### Expense Management
- Categorize expenses (meal-related and others)
- Record expense details with descriptions
- Track who recorded each expense
- Date-wise expense monitoring

### Meal Tracking
- Daily meal attendance recording
- Track meals by time (breakfast, lunch, dinner)
- Monthly meal summaries
- Automated meal cost calculations

### Reporting
- Individual member reports
- Monthly summary reports
- Meal consumption analysis
- Balance calculations (collections vs expenses)
- Per-meal cost computation

## 🛠 Technology Stack

- **Backend**: Spring Boot (Java)
- **Frontend**: Thymeleaf
- **Database**: MySQL
- **Build Tool**: Maven
- **JDBC Driver**: MySQL Connector/J

## 🗄 Database Schema

### Entities

#### MEMBER
- `member_id` (PK)
- `name`
- `phone`
- `email`
- `address`
- `password`
- `status`
- `is_manager`
- `join_date`

#### COLLECTION
- `collection_id` (PK)
- `member_id` (FK)
- `amount`
- `collect_date`
- `payment_method`
- `collected_by` (FK)
- `remarks`
- `created_at`

#### MEAL
- `meal_id` (PK)
- `meal_date`
- `total_attendances`
- `member_id` (FK)
- `created_by` (FK)
- `created_at`

#### EXPENSE
- `expense_id` (PK)
- `expense_date`
- `amount`
- `description`
- `category_id` (FK)
- `recorded_by` (FK)
- `created_at`

#### CATEGORY
- `category_id` (PK)
- `category_name`
- `description`
- `is_meal_related`
- `is_active`

### Relationships

- Member HAS_C Collection (1:M)
- Member pays Expense (1:M)
- Member mem_meal Meal (1:M)
- Meal meal_ex Expense (M:M)
- Expense belongs_to Category (1:M)

## 🚀 Installation

### Prerequisites

- Java Development Kit (JDK) 21 or higher
- MySQL Server 8.0+
- Maven
- IDE (IntelliJ IDEA)

### Steps

1. **Clone the repository**
```bash
[git clone https://github.com/yourusername/mess-management-system.git](https://github.com/masum-mir/mess-management-system.git)
cd mess-management-system
```

2. **Create MySQL database**
```sql
CREATE DATABASE mms;
```

3. **Import database schema**
```bash
mysql -u root -p mms < database/schema.sql
```

4. **Build the project**
```bash
mvn clean install
```

5. **Run the application**
```bash
mvn spring-boot:run
```

6. **Access the application**
Open your browser and navigate to: `http://localhost:8080`

## ⚙ Configuration

### Application Properties

```properties
spring.application.name=Mess-Management-System
spring.datasource.url=jdbc:mysql://localhost:3306/mms
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Thymeleaf Configuration
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
```

### Database Configuration

- **Database Name**: `mms`
- **Default Port**: `3306`
- **Default Credentials**: root/root (change in production)

## 📖 Usage

### For Managers

1. **Add Members**: Register new mess members with their details
2. **Record Collections**: Log all payment deposits from members
3. **Track Expenses**: Record daily expenses with categories
4. **Log Meals**: Enter daily meal attendance
5. **Generate Reports**: View monthly summaries and individual balances

### For Members

1. **View Meal History**: Check personal meal consumption
2. **Check Balance**: Monitor payments and dues
3. **View Expenses**: See detailed expense breakdowns

## 📸 Screenshots


### Login
<p align="center">
  <img width="800" alt="Login Page" src="https://github.com/user-attachments/assets/0af4b8ba-1ee9-4125-833c-2f4044a948ad" />
</p>

### Dashboard
<p align="center">
  <img width="800" alt="Dashboard" src="https://github.com/user-attachments/assets/e30d688a-cc81-467c-ac00-ff5ce9e9980c" />
</p>

### Members Management
<p align="center"><i>Manage member profiles, add new members, and update member information</i></p>
<p align="center">
  <img width="800" alt="Members List" src="https://github.com/user-attachments/assets/6a4909cb-ec34-47ce-8171-c09138b4df5e" />
</p>
<p align="center">
  <img width="600" alt="Add Member" src="https://github.com/user-attachments/assets/4a898572-4b4c-43aa-b814-564586e5fa54" />
</p>
<p align="center">
  <img width="600" alt="Edit Member" src="https://github.com/user-attachments/assets/15210dea-698d-4f01-9712-c5574cc7f845" />
</p>

### Payment Collections
<p align="center"><i>Track all financial deposits with payment methods and dates</i></p>
<p align="center">
  <img width="800" alt="Payment Collections List" src="https://github.com/user-attachments/assets/470d2bf1-f476-49bf-ba59-399e79941e29" />
</p>
<p align="center">
  <img width="500" alt="Add Payment" src="https://github.com/user-attachments/assets/e5bb7615-0ff8-4060-9dab-9ac69f8ea1f9" />
</p>

### Expense Management
<p align="center"><i>Record and categorize all mess expenses with detailed descriptions</i></p>
<p align="center">
  <img width="800" alt="Expense List" src="https://github.com/user-attachments/assets/c8efef42-8c88-4eb7-ae9c-1beedfda0f59" />
</p>
<p align="center">
  <img width="600" alt="Add Expense" src="https://github.com/user-attachments/assets/843a4e22-7bfd-4492-b88a-6371fcb6bba8" />
</p>
<p align="center">
  <img width="600" alt="Edit Expense" src="https://github.com/user-attachments/assets/4da94585-fa13-48f9-9453-23012c04e9a8" />
</p> 

### Meal Entry
<p align="center"><i>Log daily meal attendance for accurate cost calculations</i></p>
<p align="center">
  <img width="800" alt="Meal Entry List" src="https://github.com/user-attachments/assets/e1a3d2a3-ab1a-4eff-b1de-40bdbedd85b8" />
</p>
<p align="center">
  <img width="800" alt="Meal Details" src="https://github.com/user-attachments/assets/250c4710-3d8f-415a-b70d-70cfde366c70" />
</p>

### Reports
<p align="center"><i>Generate comprehensive monthly reports with balance summaries</i></p>
<p align="center">
  <img width="800" alt="Summary Report" src="https://github.com/user-attachments/assets/17eba1ee-d9bd-4b5f-820d-139d56c7e306" />
</p>
<p align="center">
  <img width="800" alt="Detailed Report" src="https://github.com/user-attachments/assets/f338b58f-542b-44d3-99f0-96ed572be06e" />
</p>

### Course Information
- **Course**: CSE302 - Database Management System
- **Section**: 03
- **Instructor**: Md Mostofa Kamal Rasel, Associate Professor, Dept of CSE
- **Institution**: East West University
- **Submission Date**: December 17, 2025

## 📝 License

This project is developed for educational purposes as part of a university course project.

## 🤝 Contributing

This is an academic project. For any suggestions or improvements, please contact the contributors.

---

**Note**: This system is designed for educational purposes and demonstrates database management concepts including ER modeling, normalization, indexing, and query optimization.
