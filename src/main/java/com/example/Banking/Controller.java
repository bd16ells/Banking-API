package com.example.Banking;

import com.example.Banking.user.User;
import com.example.Banking.user.UserNotFoundException;
import com.example.Banking.user.UserRepository;
import com.example.Banking.user.UserServiceImpl;
import com.example.Banking.user.history.Transaction;
import com.example.Banking.user.history.TransactionRepository;
import com.example.Banking.user.history.TransactionServiceImpl;
import com.example.Banking.user.validation.exception.BalanceTooLowException;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class Controller {

    UserServiceImpl userService;
    UserRepository userRepository;
    TransactionRepository transactionRepository;
    TransactionServiceImpl transactionService;


    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
        public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value={"/home"}, method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping("/dashboard")
    public ResponseEntity dashboard(Principal principal){
        validateUser(principal);
        Optional<User> user = userRepository.findByUsername(principal.getName());


        if(user.isPresent()){
            return new ResponseEntity<User>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);


    }

    @PostMapping("/savings/deposit")
    public ResponseEntity depositSavings(Principal principal,
                                         @RequestBody String value){
//        if(user.isPresent()){
//           return new ResponseEntity<User>(userService.depositSavings(user.get(), amount), HttpStatus.OK);
//        }
        validateUser(principal);
        JSONObject obj = new JSONObject(value);
        String temp = obj.getString("amount");

        Double amount = Double.parseDouble(temp);

        Optional<User> user = userRepository.findByUsername(principal.getName());

        if(user.isPresent()){
            userService.depositSavings(user.get(), amount);
            Transaction transaction = new Transaction("Savings", "Wallet",
                    amount, user.get().getCheckingBalance() + user.get().getSavingsBalance());
            transactionService.save(transaction);
            return new ResponseEntity<User>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);


            }


    @PostMapping("/checking/deposit")
    public ResponseEntity depositChecking(Principal principal,
                                         @RequestBody String value){
        validateUser(principal);
        JSONObject obj = new JSONObject(value);
        String temp = obj.getString("amount");

        Double amount = Double.parseDouble(temp);

        Optional<User> user = userRepository.findByUsername(principal.getName());

        if(user.isPresent()){
            userService.depositChecking(user.get(), amount);
            Transaction transaction = new Transaction("Checking", "Wallet",
                    amount, user.get().getCheckingBalance() + user.get().getSavingsBalance());
            transactionService.save(transaction);
            return new ResponseEntity<User>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);


    }



    @PostMapping("/savings/withdraw")
    public ResponseEntity withdrawSavings(Principal principal,
                                          @RequestBody String value){
        validateUser(principal);
        JSONObject obj = new JSONObject(value);
        String temp = obj.getString("amount");


        Double amount = Double.parseDouble(temp);



        Optional<User> user = userRepository.findByUsername(principal.getName());

        if(user.isPresent()){
            validateWithdrawal(user.get().getSavingsBalance(), amount);
            userService.withdrawSavings(user.get(), amount);
            Transaction transaction = new Transaction("Wallet", "Savings",
                    amount, user.get().getCheckingBalance() + user.get().getSavingsBalance());
            transactionService.save(transaction);
            return new ResponseEntity<User>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);

    }
    @PostMapping("/checking/withdraw")
    public ResponseEntity withdrawChecking(Principal principal,
                                           @RequestBody String value){
        validateUser(principal);

        JSONObject obj = new JSONObject(value);
        String temp = obj.getString("amount");
        Double amount = Double.parseDouble(temp);
        Optional<User> user = userRepository.findByUsername(principal.getName());

        if(user.isPresent()){
            try {
                validateWithdrawal(user.get().getCheckingBalance(), amount);
                userService.withdrawChecking(user.get(), amount);
                Transaction transaction = new Transaction("Wallet", "Checking",
                        amount, user.get().getCheckingBalance() + user.get().getSavingsBalance());
                transactionService.save(transaction);
            }
            catch(BalanceTooLowException e){
                return new ResponseEntity(user.get(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<User>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);

    }
    @PostMapping("/transfer")
    public ResponseEntity transfer(Principal principal,
                                  @RequestBody String values){
        validateUser(principal);
        JSONObject obj = new JSONObject(values);
        String amt = obj.getString("amount");
        String to = obj.getString("to");
        String from = obj.getString("from");


        Optional<User> user = userRepository.findByUsername(principal.getName());

        Double amount = Double.parseDouble(amt);

        if(user.isPresent()){
            if(to.equals("savings") && from.equals("checking")){
                validateWithdrawal(user.get().getCheckingBalance(), amount);
                userService.depositSavings(user.get(), amount);
                userService.withdrawChecking(user.get(), amount);
                Transaction transaction = new Transaction("Savings", "Checking",
                        amount, user.get().getCheckingBalance() + user.get().getSavingsBalance());
                transactionService.save(transaction);
                return new ResponseEntity<User>(user.get(), HttpStatus.OK);
            }
            else if(to.equals("checking") && from.equals("savings")){
                validateWithdrawal(user.get().getSavingsBalance(), amount);
                userService.depositChecking(user.get(), amount);
                userService.withdrawSavings(user.get(), amount);
                Transaction transaction = new Transaction("Checking", "Savings",
                        amount, user.get().getCheckingBalance() + user.get().getSavingsBalance());
                transactionService.save(transaction);
                return new ResponseEntity<User>(user.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> history(Principal principal){
        validateUser(principal);

        return new ResponseEntity<List<Transaction>>(transactionService.findAllByCreatedBy(principal.getName()),
                HttpStatus.OK);
    }

    @GetMapping("/logout")
    public String logout(){

        SecurityContextHolder.getContext().setAuthentication(null);
        return "login";
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
