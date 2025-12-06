package com.mms.controller;

import com.mms.model.Member;
import com.mms.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService service;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("members", service.getAll());
        return "member-list";
    }

    @PostMapping("/save")
    public String save(Member member) {
        service.save(member);
        return "redirect:/member/list";
    }



}
