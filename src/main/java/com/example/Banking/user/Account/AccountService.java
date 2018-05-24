package com.example.Banking.user.Account;

import com.example.Banking.user.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AccountService {

    public Account save(Account account) throws Exception;
    public List<Account> findByUser(User user);
}
