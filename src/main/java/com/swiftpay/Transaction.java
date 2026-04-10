package com.swiftpay;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor 
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // automatically catches the ID!
    private Long id;
	
	@ManyToOne
	//@NotNull(message="Sender is required")
	private User sender;

	@ManyToOne
	@NotNull(message="Receiver is required")
	private User receiver;

    
    @Positive(message="Amount must be greater than zero")
    @NotNull(message="Amount is required")
    private BigDecimal amount; 
    
    private String status; 
    
    private String remarks; // Remarks comes before createdAt now
	private LocalDateTime createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.now();
    }
}