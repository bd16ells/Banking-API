package com.example.Banking.user.validation.exception.message;

import com.example.Banking.user.validation.exception.BalanceTooLowException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class WebApiExceptionHandler extends ResponseEntityExceptionHandler{


    @ExceptionHandler(BalanceTooLowException.class)
    public ResponseEntity<String> handleBalanceTooLow(Exception ex){
        return new ResponseEntity<String>(ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
