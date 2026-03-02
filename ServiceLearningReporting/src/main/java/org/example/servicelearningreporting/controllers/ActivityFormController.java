package org.example.servicelearningreporting.controllers;

import org.example.servicelearningreporting.models.ActivityForm;
import org.example.servicelearningreporting.repos.ActivityFormRepo;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/activityForm")
public class ActivityFormController {
    @Autowired
    private ActivityFormRepo activityFormRepo;
    //get in progress forms
    @GetMapping("/inProgress")
    public String inProgressForms(Model model) {
        model.addAttribute("activityForm", new ActivityForm());
//        model.addAttribute("activityForms", activityFormRepo.findAll());//where submitted is false
        model.addAttribute("activityForms", activityFormRepo.findActivityFormBySubmitted(false));
        model.addAttribute("content", "pages/inProgressForms");
        return "layout";
    }
    //Get submitted Forms
    @GetMapping("/submitted")
    public String submittedForms(Model model) {
        model.addAttribute("activityForm", new ActivityForm());
        //model.addAttribute("activityForms", activityFormSubmittedRepo.findAll());//where submitted is true
        model.addAttribute("activityForms", activityFormRepo.findActivityFormBySubmitted(true));
        model.addAttribute("content", "pages/submittedForms");
        return "layout";
    }
    //In progress Post
    @PostMapping("/form-save")
    public String saveForm(@ModelAttribute ActivityForm activityFormInProgress) {
        activityFormInProgress.setSubmitted(false);
        activityFormRepo.save(activityFormInProgress);
        return "redirect:/activityForm/inProgress";
    }
    //Completed Post
    @PostMapping("/form-submit")
    public String saveFormSubmitted(@ModelAttribute ActivityForm activityFormSubmitted) {
        activityFormSubmitted.setSubmitted(true);
        activityFormRepo.save(activityFormSubmitted);
        return "redirect:/activityForm/submitted";
    }
}
