package com.swiftpay.service;

import com.swiftpay.Transaction;
import com.swiftpay.User;
import com.swiftpay.data.UserRepository;
import com.swiftpay.data.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransferService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public TransferService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * @Transactional ensures that if any part of the transfer fails, 
     * the whole operation rolls back. No money is lost!
     */
    @Transactional
    public void processTransfer(String senderUsername, String receiverUsername, BigDecimal amount) {
        
        // 1. Basic Validation
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid amount. Please enter a value greater than 0.");
        }

        if (senderUsername.equals(receiverUsername)) {
            throw new RuntimeException("You cannot send money to yourself!");
        }

        // 2. Fetch the users from the database
        User sender = userRepository.findByUsername(senderUsername);
        User receiver = userRepository.findByUsername(receiverUsername);

        if (receiver == null) {
            throw new RuntimeException("Recipient username '" + receiverUsername + "' does not exist.");
        }

        // 3. Business Logic: Check Balance
        if (sender.getBalance() == null || sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance! Your current balance is ₦" + sender.getBalance());
        }

        // 4. The Math (Atomic updates)
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        // 5. Update the database (Persistence)
        userRepository.save(sender);
        userRepository.save(receiver);

        // 6. Record the transaction history
        Transaction tx = new Transaction();
        tx.setSender(sender);
        tx.setReceiver(receiver);
        tx.setAmount(amount);
        tx.setCreatedAt(LocalDateTime.now()); // Crucial for transaction history!
        tx.setStatus("SUCCESS");
        
        // If your Transaction class has a remarks field, bind it here
        // tx.setRemarks("Transfer to " + receiver.getFullname());

        try {
            transactionRepository.save(tx);
        } catch (Exception e) {
            // If the transaction record fails, we want the whole transfer to fail
            throw new RuntimeException("Database error: Could not save transaction record.");
        }
    }
}