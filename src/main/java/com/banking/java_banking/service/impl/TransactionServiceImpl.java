package com.banking.java_banking.service.impl;

import com.banking.java_banking.dto.TransactionRequest;
import com.banking.java_banking.dto.TransactionResponse;
import com.banking.java_banking.entity.Transaction;
import com.banking.java_banking.exception.ResourceNotFoundException;
import com.banking.java_banking.repository.TransactionRepository;
import com.banking.java_banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        Transaction transaction = Transaction.builder()
                .accountNumber(request.getAccountNumber())
                .type(request.getType())
                .amount(request.getAmount())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();

        Transaction saved = transactionRepository.save(transaction);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = findOrThrow(id);
        return toResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactions(String accountNumber) {
        List<Transaction> transactions = (accountNumber != null && !accountNumber.isBlank())
                ? transactionRepository.findByAccountNumber(accountNumber)
                : transactionRepository.findAll();

        return transactions.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public TransactionResponse updateTransaction(Long id, TransactionRequest request) {
        Transaction transaction = findOrThrow(id);

        transaction.setAccountNumber(request.getAccountNumber());
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setStatus(request.getStatus());

        Transaction updated = transactionRepository.save(transaction);
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = findOrThrow(id);
        transactionRepository.delete(transaction);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Transaction findOrThrow(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", id));
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .accountNumber(transaction.getAccountNumber())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}