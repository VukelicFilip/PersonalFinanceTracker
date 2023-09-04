package com.example.personalfinancetracker.service;

import com.example.personalfinancetracker.model.Balance;
import com.example.personalfinancetracker.model.Transaction;
import com.example.personalfinancetracker.model.TransactionDTO;
import com.example.personalfinancetracker.model.TransactionType;
import com.example.personalfinancetracker.repository.BalanceRepository;
import com.example.personalfinancetracker.repository.TransactionRepository;
import com.example.personalfinancetracker.util.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repo;
    private final TransactionMapper transactionMapper;
    private final BalanceRepository balanceRepository;

    public ResponseEntity<String> create(Transaction transaction, Long userId) {
        transaction.setUserId(userId);
        repo.save(transaction);
        Optional<Balance> balanceOptional = balanceRepository.findById(userId);
        Balance balance;
        if (balanceOptional.isPresent()) {
            balance = balanceOptional.get();
        } else {
            balance = new Balance();
            balance.setUserId(userId);
        }
        if (transaction.getType().equals(TransactionType.INCOME)) {
            balance.setTotal(balance.getTotal().add(transaction.getAmount()));
            balance.setIncomeTotal(balance.getIncomeTotal().add(transaction.getAmount()));
        } else {
            balance.setTotal(balance.getTotal().subtract(transaction.getAmount()));
            balance.setExpenseTotal(balance.getExpenseTotal().add(transaction.getAmount()));
        }
        balanceRepository.save(balance);
        return ResponseEntity.ok("Transaction created successfully");
    }

    public ResponseEntity<List<TransactionDTO>> readAll(Long userId) {
        return ResponseEntity.ok(repo.findByUserId(userId));
    }

    public ResponseEntity<List<TransactionDTO>> readIncomeTransactions(Long userId) {
        return ResponseEntity.ok(repo.findByUserIdAndType(userId, TransactionType.INCOME));
    }

    public ResponseEntity<List<TransactionDTO>> readExpenseTransactions(Long userId) {
        return ResponseEntity.ok(repo.findByUserIdAndType(userId, TransactionType.EXPENSE));
    }

    public ResponseEntity<Balance> readBalance(Long userId) {
        return ResponseEntity.ok(balanceRepository.findById(userId).get());
    }

    public ResponseEntity<String> update(Long transactionId, Transaction updateInfo, Long userId) {
        Transaction toBeUpdated = repo.findByIdAndUserId(transactionId, userId).get();
        transactionMapper.update(toBeUpdated, updateInfo);
        repo.save(toBeUpdated);
        return ResponseEntity.ok("Transaction updated successfully");
    }

    public ResponseEntity<String> delete(Long transactionId, Long userId) {
        repo.deleteByIdAndUserId(transactionId, userId);
        return ResponseEntity.ok("Transaction deleted successfully");
    }

}
