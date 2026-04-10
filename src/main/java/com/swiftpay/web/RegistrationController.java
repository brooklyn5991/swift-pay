package com.swiftpay.web;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.validation.Errors;
import com.swiftpay.data.UserRepository;
import com.swiftpay.security.RegistrationForm;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm(Model model) { // 1. Add Model here
        model.addAttribute("registrationForm", new RegistrationForm()); // 2. Add a blank form
        return "registration"; 
    }
    @PostMapping
    public String processRegistration(@Valid RegistrationForm form, Errors errors) {
    	if (errors.hasErrors()) {
            return "registration"; 
        }
    	
        userRepo.save(form.toUser(passwordEncoder));
        return "redirect:/login";
    }
}