package com.example.Banking;

import com.example.Banking.user.Account.Account;
import com.example.Banking.user.Account.AccountRepository;
import com.example.Banking.user.Account.AccountService;
import com.example.Banking.user.User;
import com.example.Banking.user.UserNotFoundException;
import com.example.Banking.user.UserRepository;
import com.example.Banking.user.history.Transaction;
import com.example.Banking.user.history.TransactionServiceImpl;
import com.example.Banking.user.validation.exception.BalanceTooLowException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class ViewController {

    UserRepository userRepository;
    TransactionServiceImpl transactionService;
    AccountRepository accountRepository;
    AccountService accountService;



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

    @GetMapping("/username")
    public ResponseEntity<String> getUsername(Principal principal){
        Optional<User> user = userRepository.findByUsername(principal.getName());


        if(user.isPresent()){

            return new ResponseEntity<String>(user.get().getUsername(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/dashboard")
    public ResponseEntity<List<Account>> dashboard(Principal principal){
        validateUser(principal);
        Optional<User> user = userRepository.findByUsername(principal.getName());


        if(user.isPresent()){
            List<Account> accounts = accountService.findByUser(user.get());

            return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);


    }


    @GetMapping("/logout")
    public String logout(){

        SecurityContextHolder.getContext().setAuthentication(null);
        return "login";
    }



    @RequestMapping("/")
    public String sendToLogin(){
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model){

        model.addAttribute("loginError", true);
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
