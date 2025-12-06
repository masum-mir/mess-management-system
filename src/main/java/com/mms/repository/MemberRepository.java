package com.mms.repository;

import com.mms.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {

    @Autowired
    private JdbcTemplate jdbc;

    private final RowMapper<Member> memberMapper = (rs, rowNum) -> {
        Member m = new Member();
        m.setMemberId(rs.getInt("member_id"));
        m.setName(rs.getString("name"));
        m.setRoll(rs.getString("roll"));
        m.setPhone(rs.getString("phone"));
        m.setEmail(rs.getString("email"));
        m.setAddress(rs.getString("address"));
        m.setPassword(rs.getString("password"));
        m.setJoinDate(rs.getDate("join_date"));
        m.setStatus(rs.getString("status"));
        m.setLeader(rs.getBoolean("is_leader"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        return m;
    };

    public int save(Member m) {
        String sql = """
                INSERT INTO MEMBER
                (name, roll, phone, email, address, password, join_date, status, is_leader)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        return jdbc.update(sql,
                m.getName(),
                m.getRoll(),
                m.getPhone(),
                m.getEmail(),
                m.getAddress(),
                m.getPassword(),
                m.getJoinDate(),
                m.getStatus(),
                m.isLeader()
        );
    }

    public List<Member> findAll() {
        return jdbc.query("SELECT * FROM MEMBER ORDER BY member_id DESC", memberMapper);
    }

    public Member findById(int id) {
        String sql = "SELECT * FROM MEMBER WHERE member_id = ?";
        return jdbc.queryForObject(sql, memberMapper, id);
    }

    public int delete(int id) {
        return jdbc.update("DELETE FROM MEMBER WHERE member_id = ?", id);
    }
}
