package org.example.servicelearningreporting.controllers;

import org.example.servicelearningreporting.models.ActivityForm;
import org.example.servicelearningreporting.repos.ActivityFormRepo;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/activityForm")
public class ActivityFormController {
    @Autowired
    private ActivityFormRepo activityFormRepo;
    //get in progress forms
    @GetMapping("/inProgress")
    public String inProgressForms(Model model) {
        model.addAttribute("activityForm", new ActivityForm());
        List<ActivityForm> forms = activityFormRepo.findBySubmitted(false);
        model.addAttribute("activityForms", forms);
        model.addAttribute("content", "pages/inProgressForms");
        return "layout";
    }
    //Get submitted Forms
    @GetMapping("/submitted")
    public String submittedForms(Model model) {
        model.addAttribute("activityForm", new ActivityForm());
        List<ActivityForm> forms = activityFormRepo.findBySubmitted(true);
        model.addAttribute("activityForms", forms);
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
    //Edit submitted form
    @GetMapping("/edit/{id}")
    public String editSubmittedForm(@PathVariable Long id, Model model) {
        ActivityForm form = activityFormRepo
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid form ID: " + id));
        model.addAttribute("activityForm", form);
        model.addAttribute("readOnly", false);
        model.addAttribute("isEdit", true);
        model.addAttribute("content", "pages/student-staff-form");
        return "layout";
    }
    @PostMapping("/update")
    public String updateSubmittedForm(@ModelAttribute ActivityForm activityFormSubmitted,
                                      BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "activityForm/form";
        }
        ActivityForm existing = activityFormRepo
                .findById(activityFormSubmitted.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        //Manually set necessary fields (so submitted and id cannot be affected)
        existing.setDate(activityFormSubmitted.getDate());//activity date
        existing.setStartTime(activityFormSubmitted.getStartTime());
        existing.setEndTime(activityFormSubmitted.getEndTime());
        existing.setTotalHours(activityFormSubmitted.getTotalHours());
        existing.setOrganizationName(activityFormSubmitted.getOrganizationName());
        existing.setContactName(activityFormSubmitted.getContactName());
        existing.setCity(activityFormSubmitted.getCity());
        existing.setEmail(activityFormSubmitted.getEmail());
        existing.setPhone(activityFormSubmitted.getPhone());
        existing.setActivityDescription(activityFormSubmitted.getActivityDescription());
        existing.setDonations(activityFormSubmitted.getDonations());
        //Save
        activityFormRepo.save(existing);
        boolean isSubmitted = activityFormSubmitted.isSubmitted();
        if (isSubmitted) {
            return "redirect:/activityForm/submitted";
        }
        else {
            return "redirect:/activityForm/inProgress";
        }
    }
    //View details from submitted form
    @GetMapping("/view/{id}")
    public String viewSubmittedForm(@PathVariable Long id, Model model) {
        ActivityForm form = activityFormRepo
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid form ID: " + id));
        model.addAttribute("activityForm", form);
        model.addAttribute("readOnly", true);
        model.addAttribute("content", "pages/student-staff-form");
        return "layout";
    }
}
