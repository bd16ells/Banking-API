package com.example.Banking.user;

import com.example.Banking.user.Account.Account;
import com.example.Banking.user.Account.AccountRepository;
import com.example.Banking.user.Account.AccountService;
import com.example.Banking.user.history.Transaction;
import com.example.Banking.user.history.TransactionRepository;
import com.example.Banking.user.history.TransactionServiceImpl;
import com.example.Banking.user.history.TransactionType;
import com.example.Banking.user.validation.exception.BalanceTooLowException;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
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
public class UserController {

    UserRepository userRepository;
    TransactionRepository transactionRepository;
    TransactionServiceImpl transactionService;
    AccountRepository accountRepository;
    AccountService accountService;


    @PostMapping("/deposit/{amount}")
    public ResponseEntity<List<Account>> deposit(Principal principal,
                                         @RequestBody Optional<Account> account,
                                         @PathVariable("amount") Double amount) throws Exception {
        if (account.isPresent()) {
            BigDecimal before = account.get().getBalance();
            account.get().setBalance(new BigDecimal(amount + account.get().getBalance().doubleValue()));
            accountService.save(account.get());

            Optional<User> user = userRepository.findByUsername(principal.getName());

            Transaction transaction = new Transaction(account.get().getName(), "Deposit",before,
                    account.get().getBalance());
            transactionService.save(transaction);

            return new ResponseEntity<List<Account>>(accountService.findByUser(user.get()), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

        @PostMapping("/withdraw/{amount}")
        public ResponseEntity<List<Account>> withdraw(Principal principal, @RequestBody Optional<Account> account,
                @PathVariable("amount") Double amount) throws Exception{

            if (account.isPresent()) {
                validateWithdrawal(account.get().getBalance().doubleValue(), amount);
                BigDecimal before = account.get().getBalance();
                account.get().setBalance(new BigDecimal(account.get().getBalance().doubleValue()-amount));
                accountService.save(account.get());
                Optional<User> user = userRepository.findByUsername(principal.getName());

                Transaction transaction = new Transaction(account.get().getName(), "Withdrawal",
                        before, account.get().getBalance());
                transactionService.save(transaction);

                return new ResponseEntity<List<Account>>(accountService.findByUser(user.get()), HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }


    @PostMapping("/transfer/{name}/{amount}")
    public ResponseEntity<List<Account>> transfer(Principal principal,
                                                  @PathVariable("name")String name, // trouble type converting to Account...
                                                  @RequestBody Optional<Account> from,
                                                  @PathVariable("amount") Double amount
                                                 ) throws Exception {

        Optional<User> user = userRepository.findByUsername(principal.getName());
        Optional<Account> into = accountRepository.findByName(name);

        if (into.isPresent() && from.isPresent() && user.isPresent()) {

            validateWithdrawal(from.get().getBalance().doubleValue(), amount);
            BigDecimal fromBefore = from.get().getBalance();
            BigDecimal intoBefore = into.get().getBalance();
            from.get().setBalance(new BigDecimal(from.get().getBalance().doubleValue()-amount));
            into.get().setBalance(new BigDecimal(into.get().getBalance().doubleValue()+amount));
            accountService.save(from.get());
            accountService.save(into.get());

            Transaction transaction = new Transaction(into.get().getName(),"Deposit",
                    intoBefore, into.get().getBalance());
            Transaction transaction2 = new Transaction(from.get().getName(), "Withdrawal",
                    fromBefore, from.get().getBalance());

            transactionService.save(transaction);
            transactionService.save(transaction2);



            return new ResponseEntity<List<Account>>(accountService.findByUser(user.get()), HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    public Double getSumOfAllAccounts(User user){
        List<Account> accounts = accountService.findByUser(user);

        Double sum = 0.0;
        for(Account a : accounts){ sum+= a.getBalance().doubleValue(); }
        return sum;
    }




    private void validateUser(Principal principal){
        String username = principal.getName();
        this.userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException(username)
                );
    }

    private void validateWithdrawal(Double userFunds, Double amount){
        if(userFunds < amount){
            throw new BalanceTooLowException("insufficient funds");
        }
    }

}
