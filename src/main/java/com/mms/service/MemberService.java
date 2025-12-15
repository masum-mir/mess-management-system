package com.mms.service;

import com.mms.model.Member;
import com.mms.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public List<Member> getActiveMembers() {
        return memberRepository.findActiveMembers();
    }

    public List<Member> getManagers() {
        return memberRepository.findManagers();
    }

    public Optional<Member> getMemberById(Integer id) {
        return memberRepository.findById(id);
    }

@Transactional
public Member saveMember(Member member) {

    if (member.getPassword() == null || member.getPassword().isEmpty()) {
        throw new RuntimeException("Password cannot be null!");
    }

    List<Member> manager_id = memberRepository.findManagers();

    member.setManagerId(manager_id.getFirst().getMemberId());

    member.setPassword("MMS-" + member.getPassword());

    Integer id = memberRepository.save(member);
    member.setMemberId(id);
    return member;
}


    @Transactional
    public void updateMember(Member member) {
        memberRepository.update(member);
    }

    @Transactional
    public void deleteMember(Integer id) {
        memberRepository.delete(id);
    }

    public Integer getActiveMemberCount() {
        return memberRepository.countActiveMembers();
    }
}