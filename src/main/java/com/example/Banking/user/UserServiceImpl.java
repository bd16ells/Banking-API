package com.example.Banking.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public User depositSavings(User user, double amount) {
        user.setSavingsBalance(user.getSavingsBalance() + amount);
        return userRepository.save(user);
    }
    @Override
    public User depositChecking(User user, double amount) {
        user.setCheckingBalance(user.getCheckingBalance() + amount);
        return userRepository.save(user);
    }

    @Override
    public User withdrawSavings(User user, double amount) {
        user.setSavingsBalance(user.getSavingsBalance() - amount);
        return userRepository.save(user);
    }

    @Override
    public User withdrawChecking(User user,  double amount) {
        user.setCheckingBalance(user.getCheckingBalance() - amount);
        return userRepository.save(user);
    }

    @Override
    public double checkSavings(User user) {
        return user.getSavingsBalance();
    }

    @Override
    public double checkChecking(User user) {
        return user.getCheckingBalance();
    }

    @Override
    public void transferToSavings(User user, double amount) {

    }

    @Override
    public void transferToChecking(User user, double amount) {

    }
}
