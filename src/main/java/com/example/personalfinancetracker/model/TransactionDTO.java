package com.example.personalfinancetracker.model;


import java.math.BigDecimal;

/*
    Depending on the complexity and requirements of the project, one multiple or no DTO objects could be created.
    I opted for one DTO in this challenge in order to demonstrate its use.
     */
public interface TransactionDTO {

    Long getId();
    TransactionType getType();
    BigDecimal getAmount();
    String getDescription();

}
