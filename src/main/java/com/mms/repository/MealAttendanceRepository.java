package com.mms.repository;

import com.mms.model.MealAttendance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MealAttendanceRepository {

    private final JdbcTemplate jdbcTemplate;

    public MealAttendanceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    // Get attendance by meal ID
    public List<MealAttendance> findByMealId(Integer mealId) {
        String sql = "SELECT ma.*, m.name as member_name " +
                "FROM meal_attendance ma " +
                "JOIN member m ON ma.member_id = m.member_id " +
                "WHERE ma.meal_id = ? " +
                "ORDER BY m.name";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            MealAttendance attendance = new MealAttendance();
            attendance.setAttendanceId(rs.getInt("attendance_id"));
            attendance.setMealId(rs.getInt("meal_id"));
            attendance.setMemberId(rs.getInt("member_id"));
            attendance.setIsPresent(rs.getBoolean("is_present"));
            attendance.setMarkedBy(rs.getInt("marked_by"));
            attendance.setMarkedAt(rs.getTimestamp("marked_at").toLocalDateTime());
            return attendance;
        }, mealId);
    }

    // Save attendance
    public void save(MealAttendance attendance) {
        String sql = "INSERT INTO meal_attendance (meal_id, member_id, is_present, marked_by) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                attendance.getMealId(),
                attendance.getMemberId(),
                attendance.getIsPresent(),
                attendance.getMarkedBy());
    }

    // Delete all attendance for a meal
    public void deleteByMealId(Integer mealId) {
        String sql = "DELETE FROM meal_attendance WHERE meal_id = ?";
        jdbcTemplate.update(sql, mealId);
    }

    // Check if attendance exists
    public boolean existsByMealIdAndMemberId(Integer mealId, Integer memberId) {
        String sql = "SELECT COUNT(*) FROM meal_attendance " +
                "WHERE meal_id = ? AND member_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, mealId, memberId);
        return count != null && count > 0;
    }
}