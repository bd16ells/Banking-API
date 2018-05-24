package com.example.Banking.user.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCreatedBy(String createdBy);
}
