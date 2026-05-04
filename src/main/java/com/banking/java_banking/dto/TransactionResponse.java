package com.banking.java_banking.dto;

import com.banking.java_banking.entity.TransactionStatus;
import com.banking.java_banking.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private Long id;
    private String accountNumber;
    private TransactionType type;
    private BigDecimal amount;
    private String description;
    private TransactionStatus status;
    private LocalDateTime createdAt;
}