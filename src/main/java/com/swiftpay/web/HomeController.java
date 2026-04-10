package com.swiftpay.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.swiftpay.User;


@Controller
public class HomeController {
    
    @GetMapping("/") 
    public String home() {
        return "home";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User user, Model model) {
        // 'user' is now the actual User object from your database!
        model.addAttribute("username", user.getFullname());
        
        // In a real app, you'd get the balance from the database here
        model.addAttribute("user", user); 
        
        return "dashboard";
    }
}
    
