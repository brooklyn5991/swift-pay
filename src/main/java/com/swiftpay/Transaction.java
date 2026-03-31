package com.swiftpay;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data                // Generates getters, setters, toString, etc.
@NoArgsConstructor   // Generates an empty constructor (required by Spring)
@AllArgsConstructor  // Generates a constructor with all fields
public class Transaction {
    
    private String receiverId;
    private Double amount;
    private String remarks;
}