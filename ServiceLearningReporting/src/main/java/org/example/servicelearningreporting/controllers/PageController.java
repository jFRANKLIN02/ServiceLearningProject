package org.example.servicelearningreporting.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.servicelearningreporting.models.ActivityForm;
import org.example.servicelearningreporting.models.UserResponse;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class PageController {
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("content", "pages/home");
        return "layout";
    }
    @GetMapping("/userForm")
    public String userForm(Model model, HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute("user");
        ActivityForm form = new ActivityForm();
        form.setTimestamp(LocalDateTime.now());
        form.setFormType("Student");
        model.addAttribute("user", user);
        model.addAttribute("activityForm", form);
        model.addAttribute("readOnly", false);
        model.addAttribute("isEdit", false);
        model.addAttribute("content", "pages/student-staff-form");
        return "layout";
    }
    @GetMapping("/instructorForm")
    public String instructorForm(Model model, HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute("user");
        ActivityForm form = new ActivityForm();
        form.setTimestamp(LocalDateTime.now());
        form.setFormType("Instructor");

        if (user == null) {
            return "redirect:/login";
        }
        if (!"Instructor".equals(user.getRoleName())) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        model.addAttribute("activityForm", form);
        model.addAttribute("readOnly", false);
        model.addAttribute("isEdit", false);
        model.addAttribute("content", "pages/instructor-form");
        return "layout";
    }
}
