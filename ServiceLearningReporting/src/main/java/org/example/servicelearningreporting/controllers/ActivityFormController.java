package org.example.servicelearningreporting.controllers;

import org.example.servicelearningreporting.models.ActivityFormInProgress;
import org.example.servicelearningreporting.models.ActivityFormSubmitted;
import org.example.servicelearningreporting.repos.ActivityFormSubmittedRepo;
import org.springframework.ui.Model;
import org.example.servicelearningreporting.repos.ActivityFormInProgressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/activityForm")
public class ActivityFormController {
    @Autowired
    private ActivityFormInProgressRepo activityFormInProgressRepo;
    @Autowired
    private ActivityFormSubmittedRepo activityFormSubmittedRepo;
    //get in progress forms
    @GetMapping("/inProgress")
    public String inProgressForms(Model model) {
        model.addAttribute("activityForm", new ActivityFormInProgress());
        model.addAttribute("activityForms", activityFormInProgressRepo.findAll());
        model.addAttribute("content", "pages/inProgressForms");
        return "layout";
    }
    //Get submitted Forms
    @GetMapping("/submitted")
    public String submittedForms(Model model) {
        model.addAttribute("activityForm", new ActivityFormSubmitted());
        model.addAttribute("activityForms", activityFormSubmittedRepo.findAll());
        model.addAttribute("content", "pages/submittedForms");
        return "layout";
    }
    //In progress Post
    @PostMapping("/form-save")
    public String saveForm(@ModelAttribute ActivityFormInProgress activityFormInProgress) {
        activityFormInProgressRepo.save(activityFormInProgress);
        return "redirect:/activityForm/inProgress";
    }
    //Completed Post
    @PostMapping("/form-submit")
    public String saveFormSubmitted(@ModelAttribute ActivityFormSubmitted activityFormSubmitted) {
        activityFormSubmittedRepo.save(activityFormSubmitted);
        return "redirect:/activityForm/submitted";
    }

}
