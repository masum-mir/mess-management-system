package com.mms.controller;

import com.mms.model.Member;
import com.mms.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String listMembers(Model model) {
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        model.addAttribute("pageTitle", "All Members");
        return "member/list";
    }

//    @GetMapping("/add")
//    public String showAddForm(Model model) {
//        model.addAttribute("member", new Member());
//        model.addAttribute("pageTitle", "Add New Member");
//        model.addAttribute("leaders", memberService.getLeaders());
//        return "member";
//    }

    @PostMapping("/add")
    public String addMember(@ModelAttribute Member member,
                            RedirectAttributes redirectAttributes) {
        try {
            memberService.saveMember(member);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Member added successfully!");
            return "redirect:/members";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error adding member: " + e.getMessage());
            return "redirect:/members";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Member> member = memberService.getMemberById(id);
        if (member.isEmpty()) {
            return "redirect:/members";
        }
        model.addAttribute("member", member);
        model.addAttribute("pageTitle", "Edit Member");
        model.addAttribute("leaders", memberService.getLeaders());
        return "member/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateMember(@PathVariable Integer id,
                               @ModelAttribute Member member,
                               RedirectAttributes redirectAttributes) {
        try {
            member.setMemberId(id);
            memberService.updateMember(member);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Member updated successfully!");
            return "redirect:/members";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error updating member: " + e.getMessage());
            return "redirect:/members/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Integer id,
                               RedirectAttributes redirectAttributes) {
        try {
            memberService.deleteMember(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Member deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error deleting member: " + e.getMessage());
        }
        return "redirect:/members";
    }

    @GetMapping("/active")
    public String listActiveMembers(Model model) {
        List<Member> members = memberService.getActiveMembers();
        model.addAttribute("members", members);
        model.addAttribute("pageTitle", "Active Members");
        return "member/list";
    }
}