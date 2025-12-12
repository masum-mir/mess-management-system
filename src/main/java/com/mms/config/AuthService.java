package com.mms.config;

import com.mms.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public Member authenticate(String phone, String password) {
        Member member = authRepository.findByPhone(phone);

        if (member == null) {
            return null;
        }

        // Simple password check (in production, use BCrypt)
        if (member.getPassword().equals(password) ||
                member.getPassword().equals("$2a$10$" + password)) {

            // Check if user is active
            if (!"active".equals(member.getStatus())) {
                return null;
            }

            return member;
        }

        return null;
    }

    public Member getUserById(Integer id) {
        return authRepository.findById(id);
    }
}