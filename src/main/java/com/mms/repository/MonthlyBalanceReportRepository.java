//package com.mms.repository;
//
//import com.mms.model.MonthlyBalanceReport;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface MonthlyBalanceReportRepository extends JpaRepository<MonthlyBalanceReport, Long> {
//
//    @PersistenceContext
//    EntityManager entityManager;
//
//    @Query(value = """
//        SELECT
//            m.member_id AS memberId,
//            m.name AS name,
//            IFNULL(meals.meals_taken, 0) AS mealsTaken,
//            pmc.per_meal_cost AS perMealCost,
//            IFNULL(meals.total_meal_cost, 0) AS totalMealCost,
//            IFNULL(col.total_collection, 0) AS totalCollection,
//            pms.per_member_share AS perMemberShare,
//            (IFNULL(col.total_collection, 0) - IFNULL(meals.total_meal_cost, 0) - pms.per_member_share) AS finalBalance
//        FROM member m
//        LEFT JOIN (
//            SELECT
//                ml.member_id,
//                SUM(ml.total_attendances) AS meals_taken,
//                SUM(ml.total_attendances) *
//                IFNULL((
//                    SELECT SUM(e.amount) / NULLIF(SUM(me.total_attendances),0)
//                    FROM expense e
//                    JOIN category c ON e.category_id = c.category_id
//                    JOIN meal me ON MONTH(me.meal_date) = MONTH(e.expense_date)
//                                   AND YEAR(me.meal_date) = YEAR(e.expense_date)
//                    WHERE c.category_name = 'meal'
//                      AND MONTH(e.expense_date) = :month
//                      AND YEAR(e.expense_date) = :year
//                ),0) AS total_meal_cost
//            FROM meal ml
//            WHERE MONTH(ml.meal_date) = :month
//              AND YEAR(ml.meal_date) = :year
//            GROUP BY ml.member_id
//        ) meals ON meals.member_id = m.member_id
//        LEFT JOIN (
//            SELECT member_id, SUM(amount) AS total_collection
//            FROM collection
//            WHERE MONTH(collect_date) = :month
//              AND YEAR(collect_date) = :year
//            GROUP BY member_id
//        ) col ON col.member_id = m.member_id
//        CROSS JOIN (
//            SELECT
//                IFNULL(
//                    (
//                        SELECT SUM(e.amount)
//                        FROM expense e
//                        JOIN category c ON e.category_id = c.category_id
//                        WHERE c.category_name = 'meal'
//                          AND MONTH(e.expense_date) = :month
//                          AND YEAR(e.expense_date) = :year
//                    ) /
//                    NULLIF(
//                        (SELECT SUM(me.total_attendances)
//                         FROM meal me
//                         WHERE MONTH(me.meal_date) = :month
//                           AND YEAR(me.meal_date) = :year),0
//                    ),
//                    0
//                ) AS per_meal_cost
//        ) pmc
//        CROSS JOIN (
//            SELECT
//                IFNULL(
//                    (
//                        SELECT SUM(e.amount)
//                        FROM expense e
//                        JOIN category c ON e.category_id = c.category_id
//                        WHERE c.category_name != 'meal'
//                          AND MONTH(e.expense_date) = :month
//                          AND YEAR(e.expense_date) = :year
//                    ) /
//                    (
//                        SELECT COUNT(DISTINCT member_id)
//                        FROM member
//                        WHERE status = 'active'
//                    ),
//                    0
//                ) AS per_member_share
//        ) pms
//        WHERE m.status = 'active'
//        ORDER BY m.name
//    """, nativeQuery = true)
//    List<Object[]> getMonthlyBalanceReport(@Param("month") int month, @Param("year") int year);
//}

package com.mms.repository;

import com.mms.model.MonthlyBalanceReport;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MonthlyBalanceReportRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<MonthlyBalanceReport> getMonthlyBalanceReport(int month, int year) {
        String sql = """
            SELECT
                m.member_id AS memberId,
                m.name AS name,
                IFNULL(meals.meals_taken, 0) AS mealsTaken,
                pmc.per_meal_cost AS perMealCost,
                IFNULL(meals.total_meal_cost, 0) AS totalMealCost,
                IFNULL(col.total_collection, 0) AS totalCollection,
                pms.per_member_share AS perMemberShare,
                (IFNULL(col.total_collection, 0) - IFNULL(meals.total_meal_cost, 0) - pms.per_member_share) AS finalBalance
            FROM member m
            LEFT JOIN (
                SELECT
                    ml.member_id,
                    SUM(ml.total_attendances) AS meals_taken,
                    SUM(ml.total_attendances) *
                    IFNULL((
                        SELECT SUM(e.amount) / NULLIF(SUM(me.total_attendances),0)
                        FROM expense e
                        JOIN category c ON e.category_id = c.category_id
                        JOIN meal me ON MONTH(me.meal_date) = MONTH(e.expense_date)
                                       AND YEAR(me.meal_date) = YEAR(e.expense_date)
                        WHERE c.category_name = 'meal'
                          AND MONTH(e.expense_date) = :month
                          AND YEAR(e.expense_date) = :year
                    ),0) AS total_meal_cost
                FROM meal ml
                WHERE MONTH(ml.meal_date) = :month
                  AND YEAR(ml.meal_date) = :year
                GROUP BY ml.member_id
            ) meals ON meals.member_id = m.member_id
            LEFT JOIN (
                SELECT member_id, SUM(amount) AS total_collection
                FROM collection
                WHERE MONTH(collect_date) = :month
                  AND YEAR(collect_date) = :year
                GROUP BY member_id
            ) col ON col.member_id = m.member_id
            CROSS JOIN (
                SELECT
                    IFNULL(
                        (
                            SELECT SUM(e.amount)
                            FROM expense e
                            JOIN category c ON e.category_id = c.category_id
                            WHERE c.category_name = 'meal'
                              AND MONTH(e.expense_date) = :month
                              AND YEAR(e.expense_date) = :year
                        ) /
                        NULLIF(
                            (SELECT SUM(me.total_attendances)
                             FROM meal me
                             WHERE MONTH(me.meal_date) = :month
                               AND YEAR(me.meal_date) = :year),0
                        ),
                        0
                    ) AS per_meal_cost
            ) pmc
            CROSS JOIN (
                SELECT
                    IFNULL(
                        (
                            SELECT SUM(e.amount)
                            FROM expense e
                            JOIN category c ON e.category_id = c.category_id
                            WHERE c.category_name != 'meal'
                              AND MONTH(e.expense_date) = :month
                              AND YEAR(e.expense_date) = :year
                        ) /
                        (
                            SELECT COUNT(DISTINCT member_id)
                            FROM member
                            WHERE status = 'active'
                        ),
                        0
                    ) AS per_member_share
            ) pms
            WHERE m.status = 'active'
            ORDER BY m.name
        """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("month", month);
        query.setParameter("year", year);

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        return results.stream().map(row -> {
            MonthlyBalanceReport report = new MonthlyBalanceReport();
            report.setMemberId(((Number) row[0]).longValue());
            report.setName((String) row[1]);
            report.setMealsTaken(((Number) row[2]).intValue());
            report.setPerMealCost(new BigDecimal(row[3].toString()));
            report.setTotalMealCost(new BigDecimal(row[4].toString()));
            report.setTotalCollection(new BigDecimal(row[5].toString()));
            report.setPerMemberShare(new BigDecimal(row[6].toString()));
            report.setFinalBalance(new BigDecimal(row[7].toString()));
            return report;
        }).collect(Collectors.toList());
    }
}
