package com.swiftpay.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

    // NEW: This activates the login button
    @PostMapping("/login")
    public String processLogin() {
        // No security check yet—just a straight jump into the bank!
        return "redirect:/dashboard";
    }

    // NEW: The "Inside" of the bank
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    
}