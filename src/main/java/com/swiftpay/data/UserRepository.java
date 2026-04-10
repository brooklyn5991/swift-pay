package com.swiftpay.data;

import org.springframework.data.repository.CrudRepository;
import com.swiftpay.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}