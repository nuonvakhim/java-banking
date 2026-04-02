package com.banking.java_banking.service;

import com.banking.java_banking.dto.BankResponse;
import com.banking.java_banking.dto.CreditDebitRequest;
import com.banking.java_banking.dto.EnquiryRequest;
import com.banking.java_banking.dto.UserRequest;


public interface UserService {
    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) ;
    String nameEnquiry(EnquiryRequest enquiryRequest) ;
    BankResponse creditAccount(CreditDebitRequest request);

}
