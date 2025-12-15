package com.mms.repository;

import com.mms.model.Expense;
import com.mms.model.ExpenseShare;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ExpenseRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final RowMapper<Expense> expenseDetailRowMapper = (rs, rowNum) -> {
        Expense expense = new Expense();
        expense.setExpenseId(rs.getInt("expense_id"));
        expense.setExpenseDate(rs.getDate("expense_date").toLocalDate());
        expense.setAmount(rs.getBigDecimal("amount"));
        expense.setDescription(rs.getString("description"));
        expense.setCategoryId(rs.getInt("category_id"));
        expense.setCategoryName(rs.getString("category_name"));
        expense.setRecordedBy(rs.getInt("recorded_by"));
        expense.setRecordedByName(rs.getString("recorded_by_name"));
        expense.setMonth(rs.getInt("month"));
        expense.setYear(rs.getInt("year"));
        expense.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return expense;
    };

    public List<Expense> findAll() {
        String sql = "SELECT e.*, c.category_name, m2.name as recorded_by_name " +
                "FROM expense e " +
                "JOIN category c ON e.category_id = c.category_id " +
                "JOIN member m2 ON e.recorded_by = m2.member_id " +
                "ORDER BY e.expense_date DESC, e.expense_id DESC";
        return jdbcTemplate.query(sql, expenseDetailRowMapper);
    }

    public List<Expense> findByMonth(int month, int year) {
        String sql = "SELECT e.*, c.category_name, m2.name as recorded_by_name " +
                "FROM expense e " +
                "JOIN category c ON e.category_id = c.category_id " +
//                "JOIN member m1 ON e.paid_by_member_id = m1.member_id " +
                "JOIN member m2 ON e.recorded_by = m2.member_id " +
                "WHERE e.month = ? AND e.year = ? " +
                "ORDER BY e.expense_date DESC";
        return jdbcTemplate.query(sql, expenseDetailRowMapper, month, year);
    }

    public Expense findById(Integer id) {
        String sql = "SELECT e.*, c.category_name, m2.name as recorded_by_name " +
                "FROM expense e " +
                "JOIN category c ON e.category_id = c.category_id " +
//                "JOIN member m1 ON e.paid_by_member_id = m1.member_id " +
                "JOIN member m2 ON e.recorded_by = m2.member_id " +
                "WHERE e.expense_id = ?";
        List<Expense> expenses = jdbcTemplate.query(sql, expenseDetailRowMapper, id);
        return expenses.isEmpty() ? null : expenses.get(0);
    }

    @Transactional
    public Integer save(Expense expense) {
        String sql = "INSERT INTO expense (expense_date, amount, description, category_id, recorded_by, month, year) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, java.sql.Date.valueOf(expense.getExpenseDate()));
            ps.setBigDecimal(2, expense.getAmount());
            ps.setString(3, expense.getDescription());
            ps.setInt(4, expense.getCategoryId());
            ps.setInt(5, expense.getRecordedBy());
            ps.setInt(6, expense.getMonth());
            ps.setInt(7, expense.getYear());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void update(Expense expense) {
        String sql = "UPDATE expense SET expense_date = ?, amount = ?, description = ?, " +
                "category_id = ?, month = ?, year = ? WHERE expense_id = ?";

        jdbcTemplate.update(sql,
                java.sql.Date.valueOf(expense.getExpenseDate()),
                expense.getAmount(),
                expense.getDescription(),
                expense.getCategoryId(),
                expense.getMonth(),
                expense.getYear(),
                expense.getExpenseId()
        );
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM expense WHERE expense_id = ?";
        jdbcTemplate.update(sql, id);
    }

//    public Double findIndividualExpend(Integer expense_id) {
//        String sql = ""
//    }

//    --------------------------------------------------- delte
    // Expense Share methods
    private final RowMapper<ExpenseShare> expenseShareRowMapper = (rs, rowNum) -> {
        ExpenseShare share = new ExpenseShare();
        share.setShareId(rs.getInt("share_id"));
        share.setExpenseId(rs.getInt("expense_id"));
        share.setMemberId(rs.getInt("member_id"));
        share.setShareAmount(rs.getBigDecimal("share_amount"));
        share.setShareDate(rs.getTimestamp("share_date").toLocalDateTime());
        return share;
    };

    public void saveExpenseShare(ExpenseShare share) {
        String sql = "INSERT INTO expense_share (expense_id, member_id, share_amount) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, share.getExpenseId(), share.getMemberId(), share.getShareAmount());
    }

    public List<ExpenseShare> findSharesByExpenseId(Integer expenseId) {
        String sql = "SELECT * FROM expense_share WHERE expense_id = ?";
        return jdbcTemplate.query(sql, expenseShareRowMapper, expenseId);
    }

    public void deleteExpenseShares(Integer expenseId) {
        String sql = "DELETE FROM expense_share WHERE expense_id = ?";
        jdbcTemplate.update(sql, expenseId);
    }

//    public String shareExpansebyMealindovidual(){
//        String sql="SELECT\n" +
//                "    mem.member_id,\n" +
//                "    t.per_meal_cost\n" +
//                "FROM member mem\n" +
//                "CROSS JOIN (\n" +
//                "    SELECT \n" +
//                "        SUM(e.amount) / SUM(m.total_attendees) AS per_meal_cost\n" +
//                "    FROM expense e\n" +
//                "    JOIN meal m ON e.month = m.month\n" +
//                "    WHERE e.month = ?\n" +
//                ") t;\n";
//        return jdbcTemplate.query(sql);
//    }
//
//    public String shareExpansebyOtherCost(){
//        String sql="SELECT\n" +
//                "    SUM(e.amount) / COUNT(m.member_id) AS avg_cost_per_member\n" +
//                "FROM expense e\n" +
//                "CROSS JOIN member m\n" +
//                "WHERE e.month = '12';";
//        return jdbcTemplate.query(sql);
//    }
}