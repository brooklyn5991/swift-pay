package com.swiftpay.data;

import com.swiftpay.Transaction;
import com.swiftpay.User;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    // We now look for User objects, not String IDs
    Iterable<Transaction> findBySenderOrReceiver(User sender, User receiver);
}