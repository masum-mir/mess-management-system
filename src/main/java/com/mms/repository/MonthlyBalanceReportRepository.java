
package com.mms.repository;

import com.mms.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MonthlyBalanceReportRepository {

//    @PersistenceContext
//    public EntityManager entityManager;

    private final JdbcTemplate jdbcTemplate;

    public MonthlyBalanceReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MonthlyBalanceReport> monthlyBalanceReportMapper = (rs, rowNum) -> {
        MonthlyBalanceReport report = new MonthlyBalanceReport();

        report.setMemberId(rs.getInt("member_id"));
        report.setName(rs.getString("name"));
        report.setReportDate(rs.getDate("report_date").toLocalDate());
        report.setMealsTaken(rs.getInt("meals_taken"));
        report.setPerMealCost(rs.getBigDecimal("per_meal_cost"));
        report.setTotalMealCost(rs.getBigDecimal("total_meal_cost"));
        report.setTotalCollection(rs.getBigDecimal("total_collection"));
        report.setPerMemberShare(rs.getBigDecimal("per_member_share"));
        report.setFinalBalance(rs.getBigDecimal("final_balance"));

        return report;
    };

    public List<MonthlyBalanceReport>  getMonthlyBalanceReport(int month, int year) {
        String sql = "select * from view_member_expense_report vmer where Month(vmer.report_date) =? and YEAR(vmer.report_date)=? ";

        return jdbcTemplate.query(sql, monthlyBalanceReportMapper, month, year);
    }

//    public MonthlyBalanceReport  getMonthlyBalanceReport(Integer memberId, int month, int year) {
//        String sql = "select * from view_member_expense_report vmer where memberId=? " +
//                "and Month(vmer.report_date) =? and YEAR(vmer.report_date)=? ";
//
//        return (MonthlyBalanceReport) jdbcTemplate.query(sql, monthlyBalanceReportMapper, memberId, month, year);
//    }

    public MonthlyBalanceReport getMonthlyBalanceReport(Integer memberId, int month, int year) {

        String sql = """
        SELECT *
        FROM view_member_expense_report vmer
        WHERE vmer.member_id = ?
          AND MONTH(vmer.report_date) = ?
          AND YEAR(vmer.report_date) = ?
        """;

        return jdbcTemplate.queryForObject(
                sql,
                monthlyBalanceReportMapper,
                memberId,
                month,
                year
        );
    }


}

