package com.mms.repository;

import com.mms.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> {
        Member member = new Member();
        member.setMemberId(rs.getInt("member_id"));
        member.setName(rs.getString("name"));
        member.setPhone(rs.getString("phone"));
        member.setEmail(rs.getString("email"));
        member.setAddress(rs.getString("address"));
        member.setPassword(rs.getString("password"));
        member.setJoinDate(rs.getDate("join_date").toLocalDate());
        member.setStatus(rs.getString("status"));
        member.setIsLeader(rs.getBoolean("is_leader"));
        member.setManagerId((Integer) rs.getObject("manager_id"));
        member.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return member;
    };

    public List<Member> findAll() {
        String sql = "SELECT * FROM member ORDER BY member_id";
        return jdbcTemplate.query(sql, memberRowMapper);
    }

    public List<Member> findActiveMembers() {
        String sql = "SELECT * FROM member WHERE status = 'active' ORDER BY name";
        return jdbcTemplate.query(sql, memberRowMapper);
    }

    public Optional<Member> findById(Integer id) {
        String sql = "SELECT * FROM member WHERE member_id = ?";
        List<Member> members = jdbcTemplate.query(sql, memberRowMapper, id);
        return members.isEmpty() ? Optional.empty() : Optional.of(members.get(0));
    }

    public Optional<Member> findByPhone(String phone) {
        String sql = "SELECT * FROM member WHERE phone = ?";
        List<Member> members = jdbcTemplate.query(sql, memberRowMapper, phone);
        return members.isEmpty() ? Optional.empty() : Optional.of(members.get(0));
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        List<Member> members = jdbcTemplate.query(sql, memberRowMapper, email);
        return members.isEmpty() ? Optional.empty() : Optional.of(members.get(0));
    }

//    public Integer save(Member member) {
//        String sql = "INSERT INTO member (name, phone, email, address, password, " +
//                "join_date, status, is_leader, manager_id) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            ps.setString(1, member.getName());
//            ps.setString(3, member.getPhone());
//            ps.setString(4, member.getEmail());
//            ps.setString(5, member.getAddress());
//            ps.setString(6, member.getPassword());
//            ps.setDate(7, java.sql.Date.valueOf(member.getJoinDate()));
//            ps.setString(8, member.getStatus());
//            ps.setBoolean(9, member.getIsLeader());
//            ps.setObject(10, member.getManagerId());
//            return ps;
//        }, keyHolder);
//
//        return keyHolder.getKey().intValue();
//    }
public Integer save(Member member) {
    String sql = "INSERT INTO member (name, phone, email, address, password, " +
            "join_date, status, is_leader, manager_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();

    try {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, member.getName());
            ps.setString(3, member.getPhone());
            ps.setString(4, member.getEmail());
            ps.setString(5, member.getAddress());
            ps.setString(6, member.getPassword());

            if (member.getJoinDate() != null)
                ps.setDate(7, Date.valueOf(member.getJoinDate()));
            else
                ps.setNull(7, Types.DATE);

            ps.setString(8, member.getStatus());
            ps.setBoolean(9, member.getIsLeader());

            if (member.getManagerId() != null)
                ps.setInt(10, member.getManagerId());
            else
                ps.setNull(10, Types.INTEGER);

            return ps;
        }, keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : null;

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

    public void update(Member member) {
        String sql = "UPDATE member SET name = ?, phone = ?, email = ?, " +
                "address = ?, status = ?, is_leader = ?, manager_id = ? " +
                "WHERE member_id = ?";

        jdbcTemplate.update(sql,
                member.getName(),
                member.getPhone(),
                member.getEmail(),
                member.getAddress(),
                member.getStatus(),
                member.getIsLeader(),
                member.getManagerId(),
                member.getMemberId()
        );
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM member WHERE member_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updatePassword(Integer memberId, String newPassword) {
        String sql = "UPDATE member SET password = ? WHERE member_id = ?";
        jdbcTemplate.update(sql, newPassword, memberId);
    }

    public Integer countActiveMembers() {
        String sql = "SELECT COUNT(*) FROM member WHERE status = 'active'";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<Member> findLeaders() {
        String sql = "SELECT * FROM member WHERE is_leader = TRUE AND status = 'active'";
        return jdbcTemplate.query(sql, memberRowMapper);
    }
}