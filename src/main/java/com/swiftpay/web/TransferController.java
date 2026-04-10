package com.swiftpay.web;

import com.swiftpay.Transaction;
import com.swiftpay.User;
import com.swiftpay.data.TransactionRepository;
import com.swiftpay.service.TransferService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/transfer")
public class TransferController {

    private final TransactionRepository transactionRepo;
    private final TransferService transferService;

    @Autowired
    public TransferController(TransactionRepository transactionRepo, TransferService transferService) {
        this.transactionRepo = transactionRepo;
        this.transferService = transferService;
    }

    @GetMapping
    public String showTransferForm(Model model, @AuthenticationPrincipal User user) {
        Transaction transaction = new Transaction();
        
        // THIS LINE IS THE KEY: It creates the empty user object 
        // that will hold the username from the input field.
        transaction.setReceiver(new User()); 
        
        model.addAttribute("transaction", transaction);
        model.addAttribute("senderName", user.getFullname());
        return "transfer";
    }

    @PostMapping
    public String processTransfer(
            @Valid Transaction transaction, 
            Errors errors, 
            Model model, 
            @AuthenticationPrincipal User user,
            @RequestParam(value = "receiver.username", required = false) String receiverUsername) {

        // 1. Manual Binding Check: If the object is null, use the RequestParam
        if (transaction.getReceiver() == null || transaction.getReceiver().getUsername() == null) {
            if (receiverUsername != null && !receiverUsername.isEmpty()) {
                User receiver = new User();
                receiver.setUsername(receiverUsername);
                transaction.setReceiver(receiver);
            }
        }

        // 2. Attach the authenticated sender
        transaction.setSender(user);

        // 3. Validation Logic (ignore the 'sender' null error since we set it manually)
        if (errors.hasErrors() && !errors.hasFieldErrors("sender")) {
            model.addAttribute("senderName", user.getFullname());
            return "transfer";
        }

        try {
            transferService.processTransfer(
                user.getUsername(), 
                transaction.getReceiver().getUsername(), 
                transaction.getAmount()
            );
            return "redirect:/transfer/success";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("senderName", user.getFullname());
            return "transfer";
        }
    }
    
    @GetMapping("/success")
    public String showSuccess(Model model) {
        // Generate a random reference ID for the UI since we just redirected here
        String randomRef = "TXN-" + System.currentTimeMillis() / 1000;
        model.addAttribute("refId", randomRef);
        return "success";
    }
    
    @GetMapping("/history")
    public String showHistory(Model model, @AuthenticationPrincipal User user) {
        // Fetching history using the User object instead of a String ID
        Iterable<Transaction> history = transactionRepo.findBySenderOrReceiver(user, user);
        
        model.addAttribute("transactions", history);
        model.addAttribute("username", user.getFullname());
        
        return "history";
    }
}