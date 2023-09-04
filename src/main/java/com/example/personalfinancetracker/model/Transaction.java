package com.example.personalfinancetracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Transaction {

    @Id
    @GeneratedValue
    Long id;
    @Enumerated(EnumType.STRING)
    TransactionType type;
    @DecimalMin(value = "0.0", inclusive = false, message = "The amount has to be greater than zero")
    BigDecimal amount;
    String description;
    Long userId;

}
