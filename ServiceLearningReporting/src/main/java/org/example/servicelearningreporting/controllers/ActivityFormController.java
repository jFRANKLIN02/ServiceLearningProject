package org.example.servicelearningreporting.controllers;

import org.example.servicelearningreporting.models.ActivityForm;
import org.springframework.ui.Model;
import org.example.servicelearningreporting.repos.ActivityFormRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/activityform")
public class ActivityFormController {
    @Autowired
    private ActivityFormRepo activityFormRepo;
    @GetMapping("/list")
    public List<ActivityForm> findAll() { return activityFormRepo.findAll(); }
}
