package com.mms.config;

import com.mms.model.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    AuthService authService;

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        // If already logged in, redirect to home
        if (SessionHelper.isLoggedIn(session)) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String phone,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        Member member = authService.authenticate(phone, password);

        if (member != null) {
            SessionHelper.setLoggedInMember(session, member);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Welcome back, " + member.getName() + "!");
            session.setAttribute("loggedInMember", member);
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Invalid phone number or password!");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        SessionHelper.logout(session);
        redirectAttributes.addFlashAttribute("successMessage", "Logged out successfully!");
        return "redirect:/login";
    }
}