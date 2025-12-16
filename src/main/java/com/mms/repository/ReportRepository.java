//package com.mms.repository;
//
//import com.mms.model.dto.MemberReportDTO;
//import com.mms.model.dto.MonthlySummaryDTO;
//import com.mms.model.dto.MemberReportDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class ReportRepository {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    private final RowMapper<MemberReportDTO> memberReportRowMapper = (rs, rowNum) -> {
//        com.mms.model.dto.MemberReportDTO dto = new MemberReportDTO();
//        dto.setMemberId(rs.getInt("member_id"));
//        dto.setMemberName(rs.getString("member_name"));
//        dto.setMonth(rs.getInt("month"));
//        dto.setYear(rs.getInt("year"));
//        dto.setTotalExpenses(rs.getBigDecimal("total_expenses"));
//        dto.setTotalCollections(rs.getBigDecimal("total_collections"));
//        dto.setMealCosts(rs.getBigDecimal("meal_costs"));
//        dto.setTotalMeals(rs.getInt("total_meals"));
//        dto.calculateBalance();
//        return dto;
//    };
//
//    private final RowMapper<MonthlySummaryDTO> monthlySummaryRowMapper = (rs, rowNum) -> {
//        MonthlySummaryDTO dto = new MonthlySummaryDTO();
//        dto.setMonth(rs.getInt("month"));
//        dto.setYear(rs.getInt("year"));
//        dto.setTotalExpenses(rs.getBigDecimal("total_expenses"));
//        dto.setTotalCollections(rs.getBigDecimal("total_collections"));
//        dto.setTotalMealCosts(rs.getBigDecimal("total_meal_costs"));
//        dto.setTotalMealsServed(rs.getInt("total_meals_served"));
//        dto.setActiveMembers(rs.getInt("active_members"));
//        dto.calculateBalance();
//        return dto;
//    };
//
//    public List<MemberReportDTO> getMemberReport(int month, int year) {
//        String sql = """
//            SELECT
//                m.member_id,
//                m.name as member_name,
//                ? as month,
//                ? as year,
//                COALESCE(SUM(es.share_amount), 0) as total_expenses,
//                COALESCE((SELECT SUM(c.amount) FROM collection c
//                          WHERE c.member_id = m.member_id AND c.month = ? AND c.year = ?), 0) as total_collections,
//                COALESCE((SELECT SUM(ms.share_amount) FROM meal_share ms
//                          JOIN meal ml ON ms.meal_id = ml.meal_id
//                          WHERE ms.member_id = m.member_id AND ml.month = ? AND ml.year = ?), 0) as meal_costs,
//                COALESCE((SELECT COUNT(*) FROM meal_share ms
//                          JOIN meal ml ON ms.meal_id = ml.meal_id
//                          WHERE ms.member_id = m.member_id AND ml.month = ? AND ml.year = ?), 0) as total_meals
//            FROM member m
//            LEFT JOIN expense_share es ON m.member_id = es.member_id
//            LEFT JOIN expense e ON es.expense_id = e.expense_id AND e.month = ? AND e.year = ?
//            WHERE m.status = 'active'
//            GROUP BY m.member_id, m.name
//            ORDER BY m.name
//        """;
//
//        return jdbcTemplate.query(sql, memberReportRowMapper,
//                month, year, month, year, month, year, month, year, month, year);
//    }
//
//    public MemberReportDTO getMemberReport(Integer memberId, int month, int year) {
//        List<MemberReportDTO> reports = getMemberReport(month, year);
//        return reports.stream()
//                .filter(r -> r.getMemberId().equals(memberId))
//                .findFirst()
//                .orElse(null);
//    }
//
//    public MemberReportDTO getMemberReportById(Integer memberId, int month, int year) {
//        String sql = """
//            SELECT
//                m.member_id,
//                m.name as member_name,
//                ? as month,
//                ? as year,
//                COALESCE(SUM(es.share_amount), 0) as total_expenses,
//                COALESCE((SELECT SUM(c.amount) FROM collection c
//                          WHERE c.member_id = m.member_id AND c.month = ? AND c.year = ?), 0) as total_collections,
//                COALESCE((SELECT SUM(ms.share_amount) FROM meal_share ms
//                          JOIN meal ml ON ms.meal_id = ml.meal_id
//                          WHERE ms.member_id = m.member_id AND ml.month = ? AND ml.year = ?), 0) as meal_costs,
//                COALESCE((SELECT COUNT(*) FROM meal_share ms
//                          JOIN meal ml ON ms.meal_id = ml.meal_id
//                          WHERE ms.member_id = m.member_id AND ml.month = ? AND ml.year = ?), 0) as total_meals
//            FROM member m
//            LEFT JOIN expense_share es ON m.member_id = es.member_id
//            LEFT JOIN expense e ON es.expense_id = e.expense_id AND e.month = ? AND e.year = ?
//            WHERE m.member_id = ?
//            GROUP BY m.member_id, m.name
//        """;
//
//        List<MemberReportDTO> reports = jdbcTemplate.query(sql, memberReportRowMapper,
//                month, year, month, year, month, year, month, year, month, year, memberId);
//
//        return reports.isEmpty() ? null : reports.get(0);
//    }
//
//    public MonthlySummaryDTO getMonthlySummary(int month, int year) {
//        String sql = """
//            SELECT
//                ? as month,
//                ? as year,
//                COALESCE((SELECT SUM(amount) FROM expense WHERE month = ? AND year = ?), 0) as total_expenses,
//                COALESCE((SELECT SUM(amount) FROM collection WHERE month = ? AND year = ?), 0) as total_collections,
//                COALESCE((SELECT SUM(cost) FROM meal WHERE month = ? AND year = ?), 0) as total_meal_costs,
//                COALESCE((SELECT COUNT(*) FROM meal WHERE month = ? AND year = ?), 0) as total_meals_served,
//                (SELECT COUNT(*) FROM member WHERE status = 'active') as active_members
//        """;
//
//        List<MonthlySummaryDTO> summaries = jdbcTemplate.query(sql, monthlySummaryRowMapper,
//                month, year, month, year, month, year, month, year, month, year);
//
//        return summaries.isEmpty() ? null : summaries.get(0);
//    }
//}