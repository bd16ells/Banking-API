package com.example.Banking.user.history;

public enum TransactionType {
    WITHDRAWAL(0), DEPOSIT(1), TRANSFER(2), INITIAL_DEPOSIT(3);

    private final int value;
    TransactionType(int value){
        this.value = value;
    }

}