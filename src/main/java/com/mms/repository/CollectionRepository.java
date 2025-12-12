package com.mms.repository;

import com.mms.model.Collection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CollectionRepository {

    private final JdbcTemplate jdbcTemplate;

    public CollectionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Collection> collectionRowMapper = (rs, rowNum) -> {
        Collection collection = new Collection();
        collection.setCollectionId(rs.getInt("collection_id"));
        collection.setMemberId(rs.getInt("member_id"));
        collection.setAmount(rs.getBigDecimal("amount"));
        collection.setCollectDate(rs.getDate("collect_date").toLocalDate());
        collection.setPaymentMethod(rs.getString("payment_method"));
        collection.setCollectedBy(rs.getInt("collected_by"));
        collection.setMonth(rs.getInt("month"));
        collection.setYear(rs.getInt("year"));
        collection.setRemarks(rs.getString("remarks"));
        collection.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return collection;
    };

    private final RowMapper<Collection> collectionDetailRowMapper = (rs, rowNum) -> {
        Collection collection = new Collection();
        collection.setCollectionId(rs.getInt("collection_id"));
        collection.setMemberId(rs.getInt("member_id"));
        collection.setMemberName(rs.getString("member_name"));
        collection.setAmount(rs.getBigDecimal("amount"));
        collection.setCollectDate(rs.getDate("collect_date").toLocalDate());
        collection.setPaymentMethod(rs.getString("payment_method"));
        collection.setCollectedBy(rs.getInt("collected_by"));
        collection.setCollectedByName(rs.getString("collected_by_name"));
        collection.setMonth(rs.getInt("month"));
        collection.setYear(rs.getInt("year"));
        collection.setRemarks(rs.getString("remarks"));
        collection.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return collection;
    };

    public List<Collection> findAll() {
        String sql = "SELECT c.*, m1.name as member_name, m2.name as collected_by_name " +
                "FROM collection c " +
                "JOIN member m1 ON c.member_id = m1.member_id " +
                "JOIN member m2 ON c.collected_by = m2.member_id " +
                "ORDER BY c.collect_date DESC, c.collection_id DESC";
        return jdbcTemplate.query(sql, collectionDetailRowMapper);
    }

    public List<Collection> findByMonth(int month, int year) {
        String sql = "SELECT c.*, m1.name as member_name, m2.name as collected_by_name " +
                "FROM collection c " +
                "JOIN member m1 ON c.member_id = m1.member_id " +
                "JOIN member m2 ON c.collected_by = m2.member_id " +
                "WHERE c.month = ? AND c.year = ? " +
                "ORDER BY c.collect_date DESC";
        return jdbcTemplate.query(sql, collectionDetailRowMapper, month, year);
    }

    public List<Collection> findByMember(Integer memberId, int month, int year) {
        String sql = "SELECT c.*, m1.name as member_name, m2.name as collected_by_name " +
                "FROM collection c " +
                "JOIN member m1 ON c.member_id = m1.member_id " +
                "JOIN member m2 ON c.collected_by = m2.member_id " +
                "WHERE c.member_id = ? AND c.month = ? AND c.year = ?";
        return jdbcTemplate.query(sql, collectionDetailRowMapper, memberId, month, year);
    }

    public Collection findById(Integer id) {
        String sql = "SELECT c.*, m1.name as member_name, m2.name as collected_by_name " +
                "FROM collection c " +
                "JOIN member m1 ON c.member_id = m1.member_id " +
                "JOIN member m2 ON c.collected_by = m2.member_id " +
                "WHERE c.collection_id = ?";
        List<Collection> collections = jdbcTemplate.query(sql, collectionDetailRowMapper, id);
        return collections.isEmpty() ? null : collections.get(0);
    }

    public Integer save(Collection collection) {
        String sql = "INSERT INTO collection (member_id, amount, collect_date, payment_method, " +
                "collected_by, month, year, remarks) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, collection.getMemberId());
            ps.setBigDecimal(2, collection.getAmount());
            ps.setDate(3, java.sql.Date.valueOf(collection.getCollectDate()));
            ps.setString(4, collection.getPaymentMethod());
            ps.setInt(5, collection.getCollectedBy());
            ps.setInt(6, collection.getMonth());
            ps.setInt(7, collection.getYear());
            ps.setString(8, collection.getRemarks());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void update(Collection collection) {
        String sql = "UPDATE collection SET member_id = ?, amount = ?, collect_date = ?, " +
                "payment_method = ?, month = ?, year = ?, remarks = ? WHERE collection_id = ?";

        jdbcTemplate.update(sql,
                collection.getMemberId(),
                collection.getAmount(),
                java.sql.Date.valueOf(collection.getCollectDate()),
                collection.getPaymentMethod(),
                collection.getMonth(),
                collection.getYear(),
                collection.getRemarks(),
                collection.getCollectionId()
        );
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM collection WHERE collection_id = ?";
        jdbcTemplate.update(sql, id);
    }
}