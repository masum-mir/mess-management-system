package com.mms.repository;

import com.mms.model.Meal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class MealRepository {

    private final JdbcTemplate jdbcTemplate;

    public MealRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Meal> mealRowMapper = (rs, rowNum) -> {
        Meal meal = new Meal();
        meal.setMealId(rs.getInt("meal_id"));
        meal.setMealDate(rs.getDate("meal_date").toLocalDate());
        meal.setTotalAttendances(rs.getInt("total_attendances"));
        meal.setMemberId(rs.getInt("member_id"));
        meal.setCreatedBy(rs.getInt("created_by"));
        meal.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return meal;
    };

    /**
     * Get all meals
     */
    public List<Meal> findAll() {
        String sql = "SELECT * FROM meal ORDER BY meal_date DESC";
        return jdbcTemplate.query(sql, mealRowMapper);
    }

    /**
     * Get meals by month and year
     */
    public List<Meal> findByMonthAndYear(int month, int year) {
        String sql = "SELECT * FROM meal " +
                "WHERE MONTH(meal_date) = ? AND YEAR(meal_date) = ? " +
                "ORDER BY meal_date DESC";
        return jdbcTemplate.query(sql, mealRowMapper, month, year);
    }

    /**
     * Get meals by date
     */
    public List<Meal> findByDate(LocalDate date) {
        String sql = "SELECT * FROM meal WHERE meal_date = ? " +
                "ORDER BY meal_id DESC";
        return jdbcTemplate.query(sql, mealRowMapper, java.sql.Date.valueOf(date));
    }

    /**
     * Get meal by ID
     */
    public Meal findById(Integer id) {
        String sql = "SELECT * FROM meal WHERE meal_id = ?";
        List<Meal> meals = jdbcTemplate.query(sql, mealRowMapper, id);
        return meals.isEmpty() ? null : meals.get(0);
    }

    /**
     * Save new meal
     */
    public Integer save(Meal meal) {
        String sql = "INSERT INTO meal (meal_date, total_attendances, member_id, created_by) " +
                "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, java.sql.Date.valueOf(meal.getMealDate()));
            ps.setInt(2, meal.getTotalAttendances());
            ps.setInt(3, meal.getMemberId());
            ps.setInt(4, meal.getCreatedBy());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    /**
     * Update meal
     */
    public void update(Meal meal) {
        String sql = "UPDATE meal SET " +
                "meal_date = ?, " +
                "total_attendances = ?, " +
                "member_id = ? " +
                "WHERE meal_id = ?";

        jdbcTemplate.update(sql,
                java.sql.Date.valueOf(meal.getMealDate()),
                meal.getTotalAttendances(),
                meal.getMemberId(),
                meal.getMealId()
        );
    }

    /**
     * Update only total attendances
     * (This was your original updateMeal method)
     */
    public void updateTotalAttendances(Integer mealId, Integer totalAttendances) {
        String sql = "UPDATE meal SET total_attendances = ? WHERE meal_id = ?";
        jdbcTemplate.update(sql, totalAttendances, mealId);
    }

    /**
     * Delete meal
     */
    public void delete(Integer id) {
        String sql = "DELETE FROM meal WHERE meal_id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Count total meals for a member in a month
     */
    public int countMemberMeals(Integer memberId, int month, int year) {
        String sql = "SELECT COUNT(*) FROM meal " +
                "WHERE member_id = ? AND MONTH(meal_date) = ? AND YEAR(meal_date) = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId, month, year);
        return count != null ? count : 0;
    }

    /**
     * Get total attendances for a month
     */
    public int getTotalAttendancesByMonth(int month, int year) {
        String sql = "SELECT COALESCE(SUM(total_attendances), 0) FROM meal " +
                "WHERE MONTH(meal_date) = ? AND YEAR(meal_date) = ?";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, month, year);
        return total != null ? total : 0;
    }
}