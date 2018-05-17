package com.example.Banking.user;

public interface UserService {
    User depositChecking(User user, double amount);
    User depositSavings(User user, double amount);
    User withdrawSavings(User user, double amount);
    User withdrawChecking(User user, double amount);
    double checkSavings(User user);
    double checkChecking(User user);
    void transferToSavings(User user, double amount);
    void transferToChecking(User user, double amount);
}
