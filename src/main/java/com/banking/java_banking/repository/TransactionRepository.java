package com.banking.java_banking.repository;

import com.banking.java_banking.entity.Transaction;
import com.banking.java_banking.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountNumber(String accountNumber);

    List<Transaction> findByAccountNumberAndStatus(String accountNumber, TransactionStatus status);
}