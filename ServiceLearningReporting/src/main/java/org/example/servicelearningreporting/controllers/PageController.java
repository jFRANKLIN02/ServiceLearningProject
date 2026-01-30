package org.example.servicelearningreporting.controllers;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("content", "pages/home");
        return "layout";
    }
    @GetMapping("/activityForm")
    public String activityForm(Model model) {
        model.addAttribute("content", "pages/activityForm");
        return "layout";
    }
}
