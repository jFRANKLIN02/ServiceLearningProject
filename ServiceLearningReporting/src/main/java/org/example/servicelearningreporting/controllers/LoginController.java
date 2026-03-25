package org.example.servicelearningreporting.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.servicelearningreporting.models.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "pages/login";
    }
    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://10.157.123.50:5000/api/Users/login";

        Map<String,String> request = new HashMap<>();
        request.put("email", email);
        request.put("password", password);
        try {
            ResponseEntity<UserResponse> response = restTemplate.postForEntity(apiUrl, request, UserResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                session.setAttribute("user", response.getBody());
                return "redirect:/";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
       return "redirect:/login";
    }
    //Logout Function
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
