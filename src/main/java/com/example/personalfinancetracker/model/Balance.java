package com.example.personalfinancetracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Balance {

    @Id
    Long userId;
    BigDecimal incomeTotal;
    BigDecimal expenseTotal;
    BigDecimal total;

}
