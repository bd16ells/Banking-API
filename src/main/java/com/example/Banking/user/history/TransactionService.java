package com.example.Banking.user.history;

import java.util.List;

public interface TransactionService{
    Transaction save(Transaction transaction) throws Exception;
    List<Transaction> findAllByCreatedBy(String username);
}
