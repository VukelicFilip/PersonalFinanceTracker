package com.example.personalfinancetracker.repository;

import com.example.personalfinancetracker.model.Transaction;
import com.example.personalfinancetracker.model.TransactionDTO;
import com.example.personalfinancetracker.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByIdAndUserId(Long transactionId, Long userId);

    List<TransactionDTO> findByUserId(Long userId);

    List<TransactionDTO> findByUserIdAndType(Long userId, TransactionType transactionType);

    void deleteByIdAndUserId(Long id, Long userId);
}

