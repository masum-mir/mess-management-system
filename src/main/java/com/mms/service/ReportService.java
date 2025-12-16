package com.mms.service;

import com.mms.model.MonthlyBalanceReport;
import com.mms.repository.MonthlyBalanceReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private MonthlyBalanceReportRepository reportRepository;

    public List<MonthlyBalanceReport> getMemberReport(int month, int year) {
        return reportRepository.getMonthlyBalanceReport(month, year);
    }

    public MonthlyBalanceReport getMemberReport(Integer memberId, int month, int year) {
        return reportRepository.getMonthlyBalanceReport(memberId, month, year);
    }

//    public MemberReportDTO getMemberReport(Integer memberId, int month, int year) {
//        return reportRepository.getMemberReport(memberId, month, year);
//    }
//
//    public MemberReportDTO getMemberReportById(Integer memberId, int month, int year) {
//        return reportRepository.getMemberReportById(memberId, month, year);
//    }
//
//    public MonthlySummaryDTO getMonthlySummary(int month, int year) {
//        return reportRepository.getMonthlySummary(month, year);
//    }

//    public List<MemberReportDTO> getCurrentMonthMemberReport() {
//        LocalDate now = LocalDate.now();
//        return getMemberReport(now.getMonthValue(), now.getYear());
//    }
//
//    public MonthlySummaryDTO getCurrentMonthSummary() {
//        LocalDate now = LocalDate.now();
//        return getMonthlySummary(now.getMonthValue(), now.getYear());
//    }
}
//
//package com.mms.service;
//
//import com.mms.model.MonthlyBalanceReport;
//import com.mms.model.ReportSummary;
//import com.mms.repository.MonthlyBalanceReportRepository;
//import com.mms.repository.ReportRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.Month;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ReportService {
//
//
//    @Autowired
//    private MonthlyBalanceReportRepository reportRepository;
//
//    public List<MonthlyBalanceReport> getMonthlyReport(int month, int year) {
//        List<Object[]> results = reportRepository.getMonthlyBalanceReport(month, year);
//
//        return results.stream().map(row -> {
//            MonthlyBalanceReport report = new MonthlyBalanceReport();
//            report.setMemberId(((Number) row[0]).longValue());
//            report.setName((String) row[1]);
//            report.setMealsTaken(((Number) row[2]).intValue());
//            report.setPerMealCost(new BigDecimal(row[3].toString()));
//            report.setTotalMealCost(new BigDecimal(row[4].toString()));
//            report.setTotalCollection(new BigDecimal(row[5].toString()));
//            report.setPerMemberShare(new BigDecimal(row[6].toString()));
//            report.setFinalBalance(new BigDecimal(row[7].toString()));
//            return report;
//        }).collect(Collectors.toList());
//    }
//
//    public ReportSummary getReportSummary(List<MonthlyBalanceReport> reports, int month, int year) {
//        ReportSummary summary = new ReportSummary();
//        summary.setSelectedMonth(month);
//        summary.setSelectedYear(year);
//        summary.setMonthName(Month.of(month).name());
//
//        if (reports.isEmpty()) {
//            summary.setTotalMembers(0);
//            summary.setTotalMeals(0);
//            summary.setTotalCollection(BigDecimal.ZERO);
//            summary.setPerMealCost(BigDecimal.ZERO);
//        } else {
//            summary.setTotalMembers(reports.size());
//            summary.setTotalMeals(reports.stream()
//                    .mapToInt(MonthlyBalanceReport::getMealsTaken)
//                    .sum());
//            summary.setTotalCollection(reports.stream()
//                    .map(MonthlyBalanceReport::getTotalCollection)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add));
//            summary.setPerMealCost(reports.get(0).getPerMealCost());
//        }
//
//        return summary;
//    }
//
//
//////
//////    @Autowired
//////    private ReportRepository reportRepository;
//////
//////    public List<MonthlyReport> getMonthlyReport(int month, int year) {
//////        return reportRepository.getMonthlyReport(month, year);
//////    }
//////
//////    public MonthlyReport getMemberMonthlyReport(Integer memberId, int month, int year) {
//////        return reportRepository.getMemberMonthlyReport(memberId, month, year);
//////    }
//////
//////    /**
//////     * Calculate overall statistics for the month
//////     */
//////    public MonthlyReportSummary getMonthlyReportSummary(int month, int year) {
//////        List<MonthlyReport> reports = reportRepository.getMonthlyReport(month, year);
//////
//////        MonthlyReportSummary summary = new MonthlyReportSummary();
//////
//////        if (reports != null && !reports.isEmpty()) {
//////            // Get totals from first report (same for all members)
//////            summary.setTotalExpense(reports.get(0).getTotalExpense());
//////            summary.setTotalMeals(reports.get(0).getTotalMealsAllMembers());
//////            summary.setPerMealCost(reports.get(0).getPerMealCost());
//////
//////            // Calculate member-wise totals
//////            BigDecimal totalBill = BigDecimal.ZERO;
//////            BigDecimal totalCollected = BigDecimal.ZERO;
//////            BigDecimal totalDue = BigDecimal.ZERO;
//////            int membersWithMeals = 0;
//////            int paidMembers = 0;
//////
//////            for (MonthlyReport report : reports) {
//////                totalBill = totalBill.add(report.getTotalBill());
//////                totalCollected = totalCollected.add(report.getTotalCollected());
//////                totalDue = totalDue.add(report.getDueAmount());
//////
//////                if (report.getTotalMeals() > 0) {
//////                    membersWithMeals++;
//////                }
//////
//////                if ("PAID".equals(report.getPaymentStatus())) {
//////                    paidMembers++;
//////                }
//////            }
//////
//////            summary.setTotalBill(totalBill);
//////            summary.setTotalCollected(totalCollected);
//////            summary.setTotalDue(totalDue);
//////            summary.setMembersWithMeals(membersWithMeals);
//////            summary.setPaidMembers(paidMembers);
//////            summary.setTotalMembers(reports.size());
//////
//////            // Calculate collection percentage
//////            if (totalBill.compareTo(BigDecimal.ZERO) > 0) {
//////                summary.setCollectionPercentage(
//////                        totalCollected.multiply(new BigDecimal(100))
//////                                .divide(totalBill, 2, java.math.RoundingMode.HALF_UP)
//////                );
//////            }
//////        }
//////
//////        return summary;
//////    }
////
////    // Inner class for summary
////    public static class MonthlyReportSummary {
////        private BigDecimal totalExpense = BigDecimal.ZERO;
////        private Integer totalMeals = 0;
////        private BigDecimal perMealCost = BigDecimal.ZERO;
////        private BigDecimal totalBill = BigDecimal.ZERO;
////        private BigDecimal totalCollected = BigDecimal.ZERO;
////        private BigDecimal totalDue = BigDecimal.ZERO;
////        private BigDecimal collectionPercentage = BigDecimal.ZERO;
////        private Integer totalMembers = 0;
////        private Integer membersWithMeals = 0;
////        private Integer paidMembers = 0;
////
////        // Getters and Setters
////        public BigDecimal getTotalExpense() { return totalExpense; }
////        public void setTotalExpense(BigDecimal totalExpense) { this.totalExpense = totalExpense; }
////
////        public Integer getTotalMeals() { return totalMeals; }
////        public void setTotalMeals(Integer totalMeals) { this.totalMeals = totalMeals; }
////
////        public BigDecimal getPerMealCost() { return perMealCost; }
////        public void setPerMealCost(BigDecimal perMealCost) { this.perMealCost = perMealCost; }
////
////        public BigDecimal getTotalBill() { return totalBill; }
////        public void setTotalBill(BigDecimal totalBill) { this.totalBill = totalBill; }
////
////        public BigDecimal getTotalCollected() { return totalCollected; }
////        public void setTotalCollected(BigDecimal totalCollected) { this.totalCollected = totalCollected; }
////
////        public BigDecimal getTotalDue() { return totalDue; }
////        public void setTotalDue(BigDecimal totalDue) { this.totalDue = totalDue; }
////
////        public BigDecimal getCollectionPercentage() { return collectionPercentage; }
////        public void setCollectionPercentage(BigDecimal collectionPercentage) {
////            this.collectionPercentage = collectionPercentage;
////        }
////
////        public Integer getTotalMembers() { return totalMembers; }
////        public void setTotalMembers(Integer totalMembers) { this.totalMembers = totalMembers; }
////
////        public Integer getMembersWithMeals() { return membersWithMeals; }
////        public void setMembersWithMeals(Integer membersWithMeals) {
////            this.membersWithMeals = membersWithMeals;
////        }
////
////        public Integer getPaidMembers() { return paidMembers; }
////        public void setPaidMembers(Integer paidMembers) { this.paidMembers = paidMembers; }
////    }
//
//}

//package com.mms.service;
//
//import com.mms.model.CollectionDetail;
//import com.mms.model.MemberMonthlyReport;
//import com.mms.model.MonthlyBalanceReport;
//import com.mms.model.ReportSummary;
//import com.mms.repository.MonthlyBalanceReportRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.Month;
//import java.util.List;
//
//@Service
//public class ReportService {
//
//    @Autowired
//    private MonthlyBalanceReportRepository reportRepository;
//
//    public List<MonthlyBalanceReport> getMonthlyReport(int month, int year) {
//        return reportRepository.getMonthlyBalanceReport(month, year);
//    }
//
//    public ReportSummary getReportSummary(List<MonthlyBalanceReport> reports, int month, int year) {
//        ReportSummary summary = new ReportSummary();
//        summary.setSelectedMonth(month);
//        summary.setSelectedYear(year);
//        summary.setMonthName(Month.of(month).name());
//
//        if (reports.isEmpty()) {
//            summary.setTotalMembers(0);
//            summary.setTotalMeals(0);
//            summary.setTotalCollection(BigDecimal.ZERO);
//            summary.setPerMealCost(BigDecimal.ZERO);
//        } else {
//            summary.setTotalMembers(reports.size());
//            summary.setTotalMeals(reports.stream()
//                    .mapToInt(MonthlyBalanceReport::getMealsTaken)
//                    .sum());
//            summary.setTotalCollection(reports.stream()
//                    .map(MonthlyBalanceReport::getTotalCollection)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add));
//            summary.setPerMealCost(reports.get(0).getPerMealCost());
//        }
//
//        return summary;
//    }
//
////    public MemberMonthlyReport getMemberReport(Long memberId, int month, int year) {
////        MemberMonthlyReport report = reportRepository.getMemberReport(memberId, month, year);
////        report.setMonth(month);
////        report.setYear(year);
////        report.setMonthName(Month.of(month).name());
////
////        // Get collection details
////        List<CollectionDetail> collections =
////                reportRepository.getMemberCollections(memberId, month, year);
////        report.setCollections(collections);
////
////        return report;
////    }
//
//    public List<MemberMonthlyReport> getAllMembersReport(int month, int year) {
//        // Get all active members
//        String sql = "SELECT member_id FROM member WHERE status = 'active'";
//
//        @SuppressWarnings("unchecked")
//        List<Long> memberIds = reportRepository.entityManager
//                .createNativeQuery(sql)
//                .getResultList()
//                .stream()
//                .map(id -> ((Number) id).longValue())
//                .toList();
//
////        return memberIds.stream()
////                .map(memberId -> getMemberReport(memberId, month, year))
////                .toList();
//        return null;
//    }
//
//
//
//}

