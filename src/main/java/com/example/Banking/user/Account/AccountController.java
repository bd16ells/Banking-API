package com.example.Banking.user.Account;

import com.example.Banking.user.User;
import com.example.Banking.user.UserRepository;
import com.example.Banking.user.history.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AccountController {
    UserRepository userRepository;
    AccountRepository accountRepository;
    AccountServiceImpl accountService;
    TransactionServiceImpl transactionService;
    TransactionRepository transactionRepository;

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable("id") Optional<Account> account){

        if(account.isPresent()){
            return new ResponseEntity<>(account.get(), HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts(Principal principal) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isPresent()) {
            return new ResponseEntity<List<Account>>(accountService.findByUser(user.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/accounts/create")
    public ResponseEntity<Account> createAccount(Principal principal, @RequestBody Optional<Account> account) throws Exception{
        if(account.isPresent()) {
            Optional<User> user = userRepository.findByUsername(principal.getName());
            if (user.isPresent()) {
                account.get().setUserId(user.get().getId());
                // tries to save before persisting transaction
                accountService.save(account.get());
                Transaction transaction = new Transaction(account.get().getName(), "Initial Deposit", new BigDecimal(0),
                 account.get().getBalance());

                transactionService.save(transaction);


                return new ResponseEntity<Account>(account.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public Double getSumOfAllAccounts(User user){
        List<Account> accounts = accountService.findByUser(user);

        Double sum = 0.0;
        for(Account a : accounts){ sum+= a.getBalance().doubleValue(); }
        return sum;
    }
}
