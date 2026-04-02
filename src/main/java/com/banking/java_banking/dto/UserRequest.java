package com.banking.java_banking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {


    private String firstName;
    private String lastName;
    private String otherNames;
    private String gender;
    private String address;
    private String stateOfOrigin;
    private String accountNumber;

    private String email;
    private String password;
    private String phoneNumber;
    private String alternativePhoneNumber;
    private String status;
}
