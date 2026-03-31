package com.swiftpay.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.Errors;
import com.swiftpay.Transaction; // 1. Import your Transaction model

import jakarta.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/transfer") // 2. This controller handles anything at /transfer
public class TransferController {

    @GetMapping // 3. This handles the "GET" request (loading the page)
    public String showTransferForm(Model model) {
        
        // 4. Put a blank Transaction object in the "Model bucket"
        // This is the 'Handshake' so the HTML has a place to save data
        model.addAttribute("transaction", new Transaction());
        
        // 5. Tell Spring to show 'transfer.html'
        return "transfer";
    }
    
    @PostMapping
    public String processTransfer(@Valid Transaction transaction, Errors errors) {
        // 1. Validation Check (Just like the Taco Design)
        if (errors.hasErrors()) {
            return "transfer"; 
        }

        // 2. Logic (This is where JetoBank would talk to the database)
        log.info("Transfer Initiated: " + transaction);

        // 3. Redirect (Just like the textbook)
        // Redirecting to a success page or dashboard
        return "redirect:/transfer/success"; 
    }
    
    @GetMapping("/success")
    public String showSuccess() {
        return "success";
    }
}

