package com.example.Banking.user.Account;

import com.example.Banking.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long id);
    Optional<Account> findById(Long id);
    Optional<Account> findByName(String name);
}
