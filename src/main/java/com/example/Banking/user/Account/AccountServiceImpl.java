package com.example.Banking.user.Account;
import com.example.Banking.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @Override
    public Account save(Account account) throws Exception {
        return accountRepository.save(account);
    }

    @Override
    public List<Account> findByUser(User user) {
        return accountRepository.findByUserId(user.getId());
    }
}
