package com.example.Banking.user.history;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService{
    TransactionRepository transactionRepository;

    @Override
    public Transaction save(Transaction transaction) throws Exception {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAllByCreatedBy(String username) {
        return transactionRepository.findByCreatedBy(username);
    }
}
