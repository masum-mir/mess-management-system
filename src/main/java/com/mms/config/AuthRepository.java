package com.mms.config;

import com.mms.model.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthRepository {

    private final JdbcTemplate jdbcTemplate;

    public AuthRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> {
        Member member = new Member();
        member.setMemberId(rs.getInt("member_id"));
        member.setName(rs.getString("name"));
        member.setEmail(rs.getString("email"));
        member.setPhone(rs.getString("phone"));
        member.setPassword(rs.getString("password"));
        member.setIsManager(rs.getBoolean("is_manager"));
        member.setStatus(rs.getString("status"));
        return member;
    };

    public Member findByPhone(String phone) {
        String sql = "SELECT * FROM member WHERE phone = ? AND status = 'active'";
        List<Member> members = jdbcTemplate.query(sql, memberRowMapper, phone);
        return members.isEmpty() ? null : members.get(0);
    }

    public Member findById(Integer id) {
        String sql = "SELECT * FROM member WHERE member_id = ?";
        List<Member> members = jdbcTemplate.query(sql, memberRowMapper, id);
        return members.isEmpty() ? null : members.get(0);
    }
}