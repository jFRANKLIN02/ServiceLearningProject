package org.example.servicelearningreporting.controllers;

import org.example.servicelearningreporting.models.ActivityForm;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class PageController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("content", "pages/home");
        return "layout";
    }
//    @GetMapping("/activityForm")
//    public String activityForm(Model model) {
//        model.addAttribute("content", "pages/activityForm");
//        return "layout";
//    }
    @GetMapping("/userForm")
    public String userForm(Model model) {
        //create new object to auto fill certain fields and mark as read only?
        ActivityForm form = new ActivityForm();
        form.setTimestamp(LocalDateTime.now());
        model.addAttribute("activityForm", form);
        model.addAttribute("readOnly", false);
        model.addAttribute("isEdit", false);
        model.addAttribute("content", "pages/student-staff-form");
        return "layout";
    }
    @GetMapping("/instructorForm")
    public String instructorForm(Model model) {
        model.addAttribute("content", "pages/instructor-form");
        return "layout";
    }
}
