package com.example.Banking.user.validation.exception;

public class BalanceTooLowException extends RuntimeException {
    public BalanceTooLowException(String message) {
        super(message);
    }
}
