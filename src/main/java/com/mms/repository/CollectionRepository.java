package com.mms.repository;

import com.mms.model.Collection;
import com.mms.model.dto.MemberCollectionDto;
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
        collection.setRemarks(rs.getString("remarks"));
        collection.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return collection;
    };

    private final RowMapper<MemberCollectionDto> summaryRowMapper = (rs, rowNum) -> {
        MemberCollectionDto summary = new MemberCollectionDto();
        summary.setMemberId(rs.getInt("member_id"));
        summary.setMemberName(rs.getString("member_name"));
        summary.setTotalAmount(rs.getBigDecimal("total_amount"));
        summary.setTransactionCount(rs.getInt("transaction_count"));
        return summary;
    };


    public List<Collection> findAll() {
        String sql = "SELECT c.*, m1.name as member_name, m2.name as collected_by_name " +
                "FROM collection c " +
                "JOIN member m1 ON c.member_id = m1.member_id " +
                "JOIN member m2 ON c.collected_by = m2.member_id " +
                "ORDER BY c.collect_date DESC, c.collection_id DESC";
        return jdbcTemplate.query(sql, collectionDetailRowMapper);
    }

    public List<MemberCollectionDto> findMemberSummaryByMonth(int month, int year) {
        String sql = "SELECT " +
                "c.member_id, " +
                "m1.name as member_name, " +
                "SUM(c.amount) AS total_amount, " +
                "COUNT(*) AS transaction_count " +
                "FROM collection c " +
                "JOIN member m1 ON c.member_id = m1.member_id " +
                "WHERE Month(c.collect_date) = ? AND Year(c.collect_date) = ? and status= 'active' " +
                "GROUP BY c.member_id, m1.name " +
                "ORDER BY m1.name";
        return jdbcTemplate.query(sql, summaryRowMapper, month, year);
    }

    public List<Collection> findByMemberAndMonth(Integer memberId, int month, int year) {
        String sql = "SELECT c.*, m1.name as member_name, m2.name as collected_by_name " +
                "FROM collection c " +
                "JOIN member m1 ON c.member_id = m1.member_id " +
                "JOIN member m2 ON c.collected_by = m2.member_id " +
                "WHERE c.member_id = ? AND Month(c.collect_date) = ? AND Year(c.collect_date) = ? " +
                "ORDER BY c.collect_date DESC";
        return jdbcTemplate.query(sql, collectionDetailRowMapper, memberId, month, year);
    }


    public List<Collection> findByMonth(int month, int year) {
        String sql = "SELECT c.*, m1.name as member_name, m2.name as collected_by_name " +
                "FROM collection c " +
                "JOIN member m1 ON c.member_id = m1.member_id " +
                "JOIN member m2 ON c.collected_by = m2.member_id " +
                "WHERE Month(c.collect_date) = ? AND Year(c.collect_date) = ? " +
                "ORDER BY c.collect_date DESC";
        return jdbcTemplate.query(sql, collectionDetailRowMapper, month, year);
    }

    public List<Collection> findByMember(Integer memberId, int month, int year) {
        String sql = "SELECT c.*, m1.name as member_name, m2.name as collected_by_name " +
                "FROM collection c " +
                "JOIN member m1 ON c.member_id = m1.member_id " +
                "JOIN member m2 ON c.collected_by = m2.member_id " +
                "WHERE c.member_id = ? AND Month(c.collect_date) = ? AND Year(c.collect_date) = ?";
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
                "collected_by, remarks) VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, collection.getMemberId());
            ps.setBigDecimal(2, collection.getAmount());
            ps.setDate(3, java.sql.Date.valueOf(collection.getCollectDate()));
            ps.setString(4, collection.getPaymentMethod());
            ps.setInt(5, collection.getCollectedBy()); 
            ps.setString(6, collection.getRemarks());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void update(Collection collection) {
        String sql = "UPDATE collection SET member_id = ?, amount = ?, collect_date = ?, " +
                "payment_method = ?, remarks = ? WHERE collection_id = ?";

        jdbcTemplate.update(sql,
                collection.getMemberId(),
                collection.getAmount(),
                java.sql.Date.valueOf(collection.getCollectDate()),
                collection.getPaymentMethod(), 
                collection.getRemarks(),
                collection.getCollectionId()
        );
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM collection WHERE collection_id = ?";
        jdbcTemplate.update(sql, id);
    }
}