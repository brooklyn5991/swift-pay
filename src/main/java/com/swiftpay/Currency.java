package com.swiftpay;

import lombok.Data;
import lombok.AllArgsConstructor; 
import lombok.NoArgsConstructor;  

@Data
@AllArgsConstructor 
@NoArgsConstructor  
public class Currency {
    private String code;
    private String name;
    private double rate;
}