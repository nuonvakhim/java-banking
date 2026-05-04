package com.banking.java_banking.service;

import com.banking.java_banking.dto.TransactionRequest;
import com.banking.java_banking.dto.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse createTransaction(TransactionRequest request);

    TransactionResponse getTransactionById(Long id);

    List<TransactionResponse> getAllTransactions(String accountNumber);

    TransactionResponse updateTransaction(Long id, TransactionRequest request);

    void deleteTransaction(Long id);
}