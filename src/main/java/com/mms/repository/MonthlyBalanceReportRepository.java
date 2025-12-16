
package com.mms.repository;

import com.mms.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
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
//        report.setReportDate(rs.getDate("report_date").toLocalDate());
        Date reportDate = rs.getDate("report_date");
        if (reportDate != null) {
            report.setReportDate(reportDate.toLocalDate());
        } else {
            report.setReportDate(null);
        }
        report.setMealsTaken(rs.getInt("meals_taken"));
        report.setPerMealCost(rs.getBigDecimal("per_meal_cost"));
        report.setTotalMealCost(rs.getBigDecimal("total_meal_cost"));
        report.setTotal_cost(rs.getBigDecimal("total_cost"));
        report.setTotalCollection(rs.getBigDecimal("total_collection"));
        report.setPerMemberShare(rs.getBigDecimal("per_member_share"));
        report.setCurrentBalance(rs.getBigDecimal("current_balance"));

        return report;
    };

    public List<MonthlyBalanceReport>  getMonthlyBalanceReport() {
        String sql = "select * from view_member_expense_report";

        return jdbcTemplate.query(sql, monthlyBalanceReportMapper);
    }

    public List<MonthlyBalanceReport>  getMonthlyBalanceReport(int month, int year) {
        String sql = "\n" +
                "\n" +
                "SELECT\n" +
                "    m.member_id,\n" +
                "    m.name,\n" +
                "    IFNULL(meals.meals_taken, 0) AS meals_taken,\n" +
                "    pmc.per_meal_cost,\n" +
                "    IFNULL(meals.meals_taken * pmc.per_meal_cost, 0) AS total_meal_cost,\n" +
                "    IFNULL(col.total_collection, 0) AS total_collection,\n" +
                "    pms.per_member_share, \n" +
                "    (   IFNULL(meals.meals_taken * pmc.per_meal_cost, 0)+pms.per_member_share) as total_cost,\n" +
                "      (IFNULL(col.total_collection, 0) - IFNULL(meals.meals_taken * pmc.per_meal_cost, 0) - pms.per_member_share) AS current_balance,\n" +
                "    col.report_date\n" +
                "FROM member m\n" +
                "LEFT JOIN (\n" +
                "    -- per member meals\n" +
                "    SELECT\n" +
                "        member_id,\n" +
                "        SUM(total_attendances) AS meals_taken\n" +
                "    FROM meal\n" +
                "    WHERE MONTH(meal_date) = ?\n" +
                "      AND YEAR(meal_date) = ?\n" +
                "    GROUP BY member_id\n" +
                ") meals ON meals.member_id = m.member_id\n" +
                "LEFT JOIN (\n" +
                "    -- per member collection\n" +
                "    SELECT\n" +
                "        member_id,\n" +
                "        SUM(amount) AS total_collection,\n" +
                "        MAX(collect_date) AS report_date\n" +
                "    FROM collection\n" +
                "    WHERE MONTH(collect_date) = ?\n" +
                "      AND YEAR(collect_date) = ?\n" +
                "    GROUP BY member_id\n" +
                ") col ON col.member_id = m.member_id\n" +
                "CROSS JOIN ( \n" +
                "-- per meal cost\n" +
                "select  IFNULL(\n" +
                "            (\n" +
                "                SELECT SUM(e.amount)\n" +
                "                FROM expense e\n" +
                "                JOIN category c ON e.category_id = c.category_id\n" +
                "                WHERE c.category_name = 'meal'\n" +
                "                  AND MONTH(e.expense_date) = ?\n" +
                "                  AND YEAR(e.expense_date) = ?\n" +
                "            ) /\n" +
                "            (\n" +
                "                SELECT SUM(me.total_attendances)\n" +
                "                FROM meal me\n" +
                "                WHERE MONTH(me.meal_date) = ?\n" +
                "                  AND YEAR(me.meal_date) = ?\n" +
                "            ),\n" +
                "            0\n" +
                "        ) AS per_meal_cost \n" +
                ") pmc\n" +
                "CROSS JOIN (\n" +
                "-- per member share\n" +
                "SELECT \n" +
                "        IFNULL(\n" +
                "            (\n" +
                "                SELECT SUM(e.amount)\n" +
                "                FROM expense e\n" +
                "                JOIN category c ON e.category_id = c.category_id\n" +
                "                WHERE c.category_name != 'meal'\n" +
                "                  AND MONTH(e.expense_date) = ?\n" +
                "                  AND YEAR(e.expense_date) = ?\n" +
                "            ) /\n" +
                "            (\n" +
                "                SELECT COUNT(DISTINCT member_id)\n" +
                "                FROM member\n" +
                "                where status = 'active'\n" +
                "            ),\n" +
                "            0\n" +
                "        ) AS per_member_share\n" +
                ") pms\n" +
                "WHERE m.status = 'active'\n" +
                "ORDER BY m.member_id;\n";

        return jdbcTemplate.query(sql, monthlyBalanceReportMapper, month, year, month, year, month, year, month, year, month, year);
    }

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

