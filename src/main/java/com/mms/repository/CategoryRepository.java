package com.mms.repository;

import com.mms.model.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Category> categoryRowMapper = (rs, rowNum) -> {
        Category category = new Category();
        category.setCategoryId(rs.getInt("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setDescription(rs.getString("description"));
        category.setIsMealRelated(rs.getBoolean("is_meal_related"));
        category.setIsActive(rs.getBoolean("is_active"));
        return category;
    };

    public List<Category> findAll() {
        String sql = "SELECT * FROM category ORDER BY category_name";
        return jdbcTemplate.query(sql, categoryRowMapper);
    }

    public List<Category> findActive() {
        String sql = "SELECT * FROM category WHERE is_active = TRUE ORDER BY category_name";
        return jdbcTemplate.query(sql, categoryRowMapper);
    }

    public List<Category> findNonMealCategories() {
        String sql = "SELECT * FROM category WHERE is_meal_related = FALSE AND is_active = TRUE";
        return jdbcTemplate.query(sql, categoryRowMapper);
    }

    public Category findById(Integer id) {
        String sql = "SELECT * FROM category WHERE category_id = ?";
        List<Category> categories = jdbcTemplate.query(sql, categoryRowMapper, id);
        return categories.isEmpty() ? null : categories.get(0);
    }

    public Integer save(Category category) {
        String sql = "INSERT INTO category (category_name, description, is_meal_related, is_active) " +
                "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            ps.setBoolean(3, category.getIsMealRelated());
            ps.setBoolean(4, category.getIsActive());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void update(Category category) {
        String sql = "UPDATE category SET category_name = ?, description = ?, " +
                "is_meal_related = ?, is_active = ? WHERE category_id = ?";

        jdbcTemplate.update(sql,
                category.getCategoryName(),
                category.getDescription(),
                category.getIsMealRelated(),
                category.getIsActive(),
                category.getCategoryId()
        );
    }
}