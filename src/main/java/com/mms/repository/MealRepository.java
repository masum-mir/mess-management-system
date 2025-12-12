package com.mms.repository;

import com.mms.model.Meal;
import com.mms.model.MealAttendance;
import com.mms.model.MealShare;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
        meal.setMealType(rs.getString("meal_type"));
        meal.setCost(rs.getBigDecimal("cost"));
        meal.setDescription(rs.getString("description"));
        meal.setTotalAttendees(rs.getInt("total_attendees"));
        meal.setPerPersonCost(rs.getBigDecimal("per_person_cost"));
        meal.setMonth(rs.getInt("month"));
        meal.setYear(rs.getInt("year"));
        meal.setCreatedBy(rs.getInt("created_by"));
        meal.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return meal;
    };

    private final RowMapper<Meal> mealDetailRowMapper = (rs, rowNum) -> {
        Meal meal = new Meal();
        meal.setMealId(rs.getInt("meal_id"));
        meal.setMealDate(rs.getDate("meal_date").toLocalDate());
        meal.setMealType(rs.getString("meal_type"));
        meal.setCost(rs.getBigDecimal("cost"));
        meal.setDescription(rs.getString("description"));
        meal.setTotalAttendees(rs.getInt("total_attendees"));
        meal.setPerPersonCost(rs.getBigDecimal("per_person_cost"));
        meal.setMonth(rs.getInt("month"));
        meal.setYear(rs.getInt("year"));
        meal.setCreatedBy(rs.getInt("created_by"));
        meal.setCreatedByName(rs.getString("created_by_name"));
        meal.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return meal;
    };

    public List<Meal> findAll() {
        String sql = "SELECT m.*, mem.name as created_by_name " +
                "FROM meal m " +
                "JOIN member mem ON m.created_by = mem.member_id " +
                "ORDER BY m.meal_date DESC, m.meal_type";
        return jdbcTemplate.query(sql, mealDetailRowMapper);
    }

    public List<Meal> findByMonth(int month, int year) {
        String sql = "SELECT m.*, mem.name as created_by_name " +
                "FROM meal m " +
                "JOIN member mem ON m.created_by = mem.member_id " +
                "WHERE m.month = ? AND m.year = ? " +
                "ORDER BY m.meal_date DESC, m.meal_type";
        return jdbcTemplate.query(sql, mealDetailRowMapper, month, year);
    }

    public Meal findById(Integer id) {
        String sql = "SELECT m.*, mem.name as created_by_name " +
                "FROM meal m " +
                "JOIN member mem ON m.created_by = mem.member_id " +
                "WHERE m.meal_id = ?";
        List<Meal> meals = jdbcTemplate.query(sql, mealDetailRowMapper, id);
        return meals.isEmpty() ? null : meals.get(0);
    }

    @Transactional
    public Integer save(Meal meal) {
        String sql = "INSERT INTO meal (meal_date, meal_type, cost, description, " +
                "total_attendees, per_person_cost, month, year, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        System.out.println("KeyHolder::: "+keyHolder);
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, java.sql.Date.valueOf(meal.getMealDate()));
            ps.setString(2, meal.getMealType());
            ps.setBigDecimal(3, meal.getCost());
            ps.setString(4, meal.getDescription());
            ps.setInt(5, meal.getTotalAttendees());
            ps.setBigDecimal(6, meal.getPerPersonCost());
            ps.setInt(7, meal.getMonth());
            ps.setInt(8, meal.getYear());
            ps.setInt(9, meal.getCreatedBy());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void update(Meal meal) {
        String sql = "UPDATE meal SET meal_date = ?, meal_type = ?, cost = ?, description = ?, " +
                "total_attendees = ?, per_person_cost = ?, month = ?, year = ? WHERE meal_id = ?";

        jdbcTemplate.update(sql,
                java.sql.Date.valueOf(meal.getMealDate()),
                meal.getMealType(),
                meal.getCost(),
                meal.getDescription(),
                meal.getTotalAttendees(),
                meal.getPerPersonCost(),
                meal.getMonth(),
                meal.getYear(),
                meal.getMealId()
        );
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM meal WHERE meal_id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Meal Attendance methods
    private final RowMapper<MealAttendance> attendanceRowMapper = (rs, rowNum) -> {
        MealAttendance attendance = new MealAttendance();
        attendance.setAttendanceId(rs.getInt("attendance_id"));
        attendance.setMealId(rs.getInt("meal_id"));
        attendance.setMemberId(rs.getInt("member_id"));
        attendance.setIsPresent(rs.getBoolean("is_present"));
        attendance.setMarkedBy(rs.getInt("marked_by"));
        attendance.setMarkedAt(rs.getTimestamp("marked_at").toLocalDateTime());
        return attendance;
    };

    public void saveAttendance(MealAttendance attendance) {
        String sql = "INSERT INTO meal_attendance (meal_id, member_id, is_present, marked_by) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, attendance.getMealId(), attendance.getMemberId(),
                attendance.getIsPresent(), attendance.getMarkedBy());
    }

    public List<MealAttendance> findAttendanceByMealId(Integer mealId) {
        String sql = "SELECT * FROM meal_attendance WHERE meal_id = ?";
        return jdbcTemplate.query(sql, attendanceRowMapper, mealId);
    }

    public void deleteAttendance(Integer mealId) {
        String sql = "DELETE FROM meal_attendance WHERE meal_id = ?";
        jdbcTemplate.update(sql, mealId);
    }

    // Meal Share methods
    private final RowMapper<MealShare> mealShareRowMapper = (rs, rowNum) -> {
        MealShare share = new MealShare();
        share.setMealShareId(rs.getInt("meal_share_id"));
        share.setMealId(rs.getInt("meal_id"));
        share.setMemberId(rs.getInt("member_id"));
        share.setShareAmount(rs.getBigDecimal("share_amount"));
        share.setCalculatedAt(rs.getTimestamp("calculated_at").toLocalDateTime());
        return share;
    };

    public void saveMealShare(MealShare share) {
        String sql = "INSERT INTO meal_share (meal_id, member_id, share_amount) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, share.getMealId(), share.getMemberId(), share.getShareAmount());
    }

    public List<MealShare> findSharesByMealId(Integer mealId) {
        String sql = "SELECT * FROM meal_share WHERE meal_id = ?";
        return jdbcTemplate.query(sql, mealShareRowMapper, mealId);
    }

    public void deleteMealShares(Integer mealId) {
        String sql = "DELETE FROM meal_share WHERE meal_id = ?";
        jdbcTemplate.update(sql, mealId);
    }
}