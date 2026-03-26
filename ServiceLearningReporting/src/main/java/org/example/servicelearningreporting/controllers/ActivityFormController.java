package org.example.servicelearningreporting.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.servicelearningreporting.models.ActivityForm;
import org.example.servicelearningreporting.models.UserResponse;
import org.example.servicelearningreporting.repos.ActivityFormRepo;
import org.example.servicelearningreporting.services.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/activityForm")
public class ActivityFormController {
    private final PdfService pdfService;
    @Autowired
    private ActivityFormRepo activityFormRepo;

    public ActivityFormController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    //get in progress forms
    @GetMapping("/inProgress")
    public String inProgressForms(Model model, HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute("user");

        model.addAttribute("activityForm", new ActivityForm());
        List<ActivityForm> forms = activityFormRepo.findBySubmittedAndUserID(false, user.getUserID());
        model.addAttribute("activityForms", forms);
        model.addAttribute("content", "pages/inProgressForms");
        return "layout";
    }
    //Get submitted Forms
    @GetMapping("/submitted")
    public String submittedForms(Model model, HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute("user");

        model.addAttribute("activityForm", new ActivityForm());
        List<ActivityForm> forms = activityFormRepo.findBySubmittedAndUserID(true, user.getUserID());
        model.addAttribute("activityForms", forms);
        model.addAttribute("content", "pages/submittedForms");
        return "layout";
    }
    //In progress Post
    @PostMapping("/form-save")
    public String saveForm(@ModelAttribute ActivityForm activityFormInProgress, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        UserResponse user = (UserResponse) session.getAttribute("user");
        try {
            activityFormInProgress.setSubmitted(false);
            activityFormInProgress.setUserID(user.getUserID());
            activityFormRepo.save(activityFormInProgress);
            model.addAttribute("successMessage", "Save Successful");
        }
        catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to Save");
        }
        return "redirect:/activityForm/inProgress";
    }
    //Completed Post
    @PostMapping("/form-submit")
    public String saveFormSubmitted(@ModelAttribute ActivityForm activityFormSubmitted, HttpSession session, RedirectAttributes redirectAttributes) {
        UserResponse user = (UserResponse) session.getAttribute("user");
        try {
            activityFormSubmitted.setSubmitted(true);
            activityFormSubmitted.setUserID(user.getUserID());
            activityFormRepo.save(activityFormSubmitted);
            redirectAttributes.addFlashAttribute("successMessage", "Submit Succesful");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to Submit. Please Try Again");
        }
        return "redirect:/activityForm/submitted";
    }
    //Edit submitted form
    @GetMapping("/edit/{id}")
    public String editSubmittedForm(@PathVariable Long id, Model model, HttpSession session) {
        ActivityForm form = activityFormRepo
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid form ID: " + id));
        boolean isSubmitted = form.isSubmitted();
        if (isSubmitted) {
            model.addAttribute("isSubmmitted", true);
        }
        else {
            model.addAttribute("isSubmitted", false);
        }
        System.out.println("isSubmitted: " + isSubmitted);
        form.setTimestamp(LocalDateTime.now());
        model.addAttribute("activityForm", form);
        model.addAttribute("readOnly", false);
        model.addAttribute("isEdit", true);
        model.addAttribute("content", "pages/student-staff-form");
        return "layout";
    }
    @PostMapping("/update")
    public String updateSubmittedForm(@ModelAttribute ActivityForm activityFormSubmitted,
                                      BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
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
            redirectAttributes.addFlashAttribute("successMessage", "Update Successful");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Update Failed. Please Try Again");
        }
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
    public String viewSubmittedForm(@PathVariable Long id, Model model, HttpSession session) {
        ActivityForm form = activityFormRepo
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid form ID: " + id));
        model.addAttribute("activityForm", form);
        model.addAttribute("readOnly", true);
        model.addAttribute("content", "pages/student-staff-form");
        return "layout";
    }
    //New Details Page
    @GetMapping("/details/{id}")
    public String viewDetails(@PathVariable Long id, Model model, HttpSession session) {
        ActivityForm activity = activityFormRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid form ID: " + id));
        model.addAttribute("activityForm", activity);
        model.addAttribute("content",  "pages/details");
        return "layout";
    }
    //Post Mapping for PDF
    @PostMapping("/print/{id}")
    public ResponseEntity<byte[]> printActivity(
            @PathVariable long id,
            @RequestParam(required = false) List<String> fields) throws Exception {

        ActivityForm activity = activityFormRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid form ID: " + id));
        byte[] pdf = pdfService.generatePdf(activity, fields);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "activity-report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }
    //PDF Controller
    @PostMapping("/preview/{id}")
    public String previewPdf(
            @PathVariable long id,
            @RequestParam(required = false) List<String> fields,
            Model model) {

        ActivityForm activity = activityFormRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid form ID: " + id));

        model.addAttribute("activityForm", activity);
        model.addAttribute("fields", fields);

        return "pdf/activity-preview";
        //return "pdf/activity-pdf";
    }
}
