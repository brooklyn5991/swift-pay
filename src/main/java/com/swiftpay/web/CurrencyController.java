package com.swiftpay.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;          
import java.util.Arrays;       
import com.swiftpay.Currency;

@Controller
public class CurrencyController {
	@PostMapping("/currencies")
	public String calculateCurrencies(@RequestParam("amount") double amount, Model model) {
	    // 1. Re-create the list (In Chapter 3, we'll get this from a Database!)
	    List<Currency> currencies = Arrays.asList(
	        new Currency("EUR", "Euro", 0.92),
	        new Currency("GBP", "British Pound", 0.79),
	        new Currency("NGN", "Nigerian Naira", 1500.0)
	    );

	    // 2. Put the NEW amount into the model
	    model.addAttribute("currencies", currencies);
	    model.addAttribute("balance", amount);

	    // 3. Show the same page, but with the new math!
	    return "currencies";
	}
	
}
