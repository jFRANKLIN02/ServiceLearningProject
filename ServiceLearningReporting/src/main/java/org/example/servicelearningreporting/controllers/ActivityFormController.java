package org.example.servicelearningreporting.controllers;

import org.example.servicelearningreporting.models.ActivityFormInProgress;
import org.springframework.ui.Model;
import org.example.servicelearningreporting.repos.ActivityFormInProgressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/activityForm")
public class ActivityFormController {
    @Autowired
    private ActivityFormInProgressRepo activityFormRepo;
    //get in progress forms
    @GetMapping("/inProgress")
    public String inProgressForms(Model model) {
        model.addAttribute("activityForm", new ActivityFormInProgress());
        model.addAttribute("activityForms", activityFormRepo.findAll());
        model.addAttribute("content", "pages/inProgressForms");
        return "layout";
    }
}
