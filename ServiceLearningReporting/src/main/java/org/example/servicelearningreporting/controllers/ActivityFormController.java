package org.example.servicelearningreporting.controllers;

import org.example.servicelearningreporting.models.ActivityForm;
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
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public String inProgressForms(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            Model model) {
        model.addAttribute("activityForm", new ActivityForm());
        List<ActivityForm> forms;
        if (search != null && !search.isEmpty()) {
            forms = activityFormRepo.searchInProgressForms(search);
        }
        else if ("asc".equals(sort)) {
            forms = activityFormRepo.findBySubmittedOrderByDateAsc(false);
        } else if ("desc".equals(sort)) {
            forms = activityFormRepo.findBySubmittedOrderByDateDesc(false);
        } else {
            forms = activityFormRepo.findBySubmitted(false);
        }
        model.addAttribute("activityForms", forms);
        model.addAttribute("content", "pages/inProgressForms");
        return "layout";
    }
    //Get submitted Forms
    @GetMapping("/submitted")
    public String submittedForms(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            Model model) {
        model.addAttribute("activityForm", new ActivityForm());
        List<ActivityForm> forms;
        if (search != null && !search.isEmpty()) {
            forms = activityFormRepo.searchSubmittedForms(search);
        }
        else if ("asc".equals(sort)) {
            forms = activityFormRepo.findBySubmittedOrderByDateAsc(true);
        } else if ("desc".equals(sort)) {
            forms = activityFormRepo.findBySubmittedOrderByDateDesc(true);
        } else {
            forms = activityFormRepo.findBySubmitted(true);
        }
        model.addAttribute("activityForms", forms);
        model.addAttribute("content", "pages/submittedForms");
        return "layout";
    }
    //In progress Post
    @PostMapping("/form-save")
    public String saveForm(
            @ModelAttribute ActivityForm activityFormInProgress,
            @RequestParam(value = "images", required = false) MultipartFile[] images) throws Exception {
        handleImageUpload(activityFormInProgress, List.of(images));
        activityFormInProgress.setSubmitted(false);
        activityFormRepo.save(activityFormInProgress);
        return "redirect:/activityForm/inProgress";
    }
    //Completed Post
    @PostMapping("/form-submit")
    public String saveFormSubmitted(
            @ModelAttribute ActivityForm activityFormSubmitted,
            @RequestParam(value = "images", required = false) MultipartFile[] images) throws Exception {
        handleImageUpload(activityFormSubmitted, List.of(images));
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
        form.setTimestamp(LocalDateTime.now());
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
    //New Details Page
    @GetMapping("/details/{id}")
    public String viewDetails(@PathVariable Long id, Model model) {
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
    //Image uploading
    private void handleImageUpload(ActivityForm form, List<MultipartFile> images) throws Exception {
        if (images.size() > 5) {
            throw new RuntimeException("Maximum 5 images allowed");
        }
        List<String> paths = new ArrayList<>();
        for (MultipartFile file : images) {
            if (file.isEmpty()) continue;
            //size check
            if (file.getSize() > 3 * 1024 * 1024) {
                throw new RuntimeException("File exceeds 3MB");
            }
            //type check
            String contentType = file.getContentType();
            if (!contentType.equals("image/png") &&
                    !contentType.equals("image/jpeg") &&
                    !contentType.equals("image/tiff")) {

                throw new RuntimeException("Only PNG, JPG, TIFF allowed");
            }
            //save
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            paths.add("/uploads/" + fileName);
        }
        form.setImagePaths(paths);
    }
}
