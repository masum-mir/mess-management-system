package com.mms.service;

import com.mms.model.Member;
import com.mms.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository repo;

    public int save(Member m) {
        return repo.save(m);
    }

    public List<Member> getAll() {
        return repo.findAll();
    }

    public Member getById(int id) {
        return repo.findById(id);
    }

    public int deleteMember(int id) {
        return repo.delete(id);
    }
}
